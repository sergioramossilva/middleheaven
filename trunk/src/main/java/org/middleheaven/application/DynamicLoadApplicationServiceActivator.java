package org.middleheaven.application;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashSet;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import org.middleheaven.core.Container;
import org.middleheaven.core.bootstrap.BootstapListener;
import org.middleheaven.core.bootstrap.BootstrapEvent;
import org.middleheaven.core.bootstrap.BootstrapService;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.core.wiring.activation.ActivationContext;
import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;
import org.middleheaven.core.wiring.annotations.Wire;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.FileChangeEvent;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.WatchableContainer;
import org.middleheaven.logging.LogBook;
import org.middleheaven.logging.LoggingService;
import org.middleheaven.util.classification.BooleanClassifier;

/**
 * Provides an {@link ApplicationLoadingService} for loading application modules present at the application configuration path
 * Application Modules are jar files with extension .apm that contain a manifest file
 * with an {@code Application-Module} entry pointing to the {@link ApplicationModule} class.
 * 
 * 
 */
public class DynamicLoadApplicationServiceActivator extends Activator implements BootstapListener  {


	private LogBook log;

	private LoggingService loggingService;
	private BootstrapService bootstrapService;
	private ApplicationLoadingService applicationLoadingCycleService;

	private WiringService wiringService;
	
	public static void setAppModulesFilter(BooleanClassifier<ManagedFile> appModulesFilter) {
		DynamicLoadApplicationServiceActivator.appModulesFilter = appModulesFilter;
	}

	private static BooleanClassifier<ManagedFile> appModulesFilter =  new BooleanClassifier<ManagedFile>(){

		@Override
		public Boolean classify(ManagedFile file) {
			return file.getName().endsWith(".apm");
		}
	};
	
	public DynamicLoadApplicationServiceActivator(){}
	
	@Wire
	public void setBootstrapService(BootstrapService bootstrapService) {
		this.bootstrapService = bootstrapService;
	}

	@Wire
	public void setLoggingService(LoggingService loggingService) {
		this.loggingService = loggingService;
	}
	
	@Wire
	public void setWiringService(WiringService wiringService) {
		this.wiringService = wiringService;
	}
	
	@Publish
	public ApplicationLoadingService getApplicationLoadingCycleService() {
		return applicationLoadingCycleService;
	}

	@Override
	public void activate(ActivationContext context) {
		log = loggingService.getLogBook(this.getClass().getName());
	
		bootstrapService.addListener(this);
		applicationLoadingCycleService =  new DynamicLoadApplicationService( bootstrapService.getEnvironmentBootstrap().getContainer());
	}

	@Override
	public void inactivate(ActivationContext context) {
		cycle.stop();
		log=null;
	}
	
	@Override
	public void onBoostapEvent(BootstrapEvent event) {
		if(event.isBootup() && event.isEnd()){
			// start cycle
			cycle.start();
		} else if (event.isBootdown() && event.isStart()){
			// end cycle
			cycle.stop();
		}
	};
	
	private DefaultApplicationLoadingCycle cycle;
	
	private class DynamicLoadApplicationService implements ApplicationLoadingService {
		TransientApplicationContext context;
		
		public DynamicLoadApplicationService(Container container) {
			context = new TransientApplicationContext(wiringService.getObjectPool(),container);
			cycle = new DinamicLoadingCycle(context);
		}
		
		@Override
		public ApplicationLoadingCycle getApplicationLoadingCycle() {
			return cycle;
		}
		
		@Override
		public ApplicationContext getApplicationContext(){
			return context;
		}
		
	}

	private class DinamicLoadingCycle extends DefaultApplicationLoadingCycle{
		private TransientApplicationContext context;

		public DinamicLoadingCycle(TransientApplicationContext context) {
			super();
			this.context = context;
		}

		@Override
		public void start() {
			log.info("Scanning for applications");
			this.setState(ApplicationCycleState.STOPED);
			loadPresentModules();
			log.info("Activating applications");
			for (ApplicationModule module : context.modules()){
				try {
					module.load(context);
				} catch (RuntimeException e){
					log.error("Impossible to activate " + module.getModuleID(), e);
					throw e;
				}
			}
			this.setState(ApplicationCycleState.LOADED);
			this.setState(ApplicationCycleState.READY);
			log.info("Applications ready");
		}

		@Override
		public void stop() {
			this.setState(ApplicationCycleState.PAUSED);
			log.info("Deactivating applications");
			for (ApplicationModule module : context.modules()){
				try {
					module.unload(context);
				} catch (Exception e){
					log.warn("Impossible to deactivate " + module.getModuleID(), e);
				}
			}
			this.setState(ApplicationCycleState.STOPED);
			log.info("Applications deactivated");
		}

		@Override
		public void onChange(FileChangeEvent event) {
			ManagedFile file = event.getFile();
			if (appModulesFilter.classify(file)){
				this.setState(ApplicationCycleState.PAUSED);
				loadModuleFromFile(file);
				this.setState(ApplicationCycleState.READY);
			}
		}
		

		protected void loadPresentModules(){

			// look for all files ending in .apm (Application Module)
			// these must be jar files
			Collection<ManagedFile> applicationModuleFiles = new HashSet<ManagedFile>();

			ManagedFile f =  bootstrapService.getEnvironmentBootstrap().getContainer().getAppConfigRepository();

			if (f.isWatchable()){
				WatchableContainer wr = (WatchableContainer)f;
				wr.addFileChangelistener(cycle, f);
			}

			// filter apm only (apm are jar files)
			applicationModuleFiles.addAll( f.listFiles(appModulesFilter));

			for (ManagedFile jar : applicationModuleFiles){
				loadModuleFromFile(jar);
			}

		}

		private void loadModuleFromFile(ManagedFile jar) {

			try{
				URLClassLoader cloader = URLClassLoader.newInstance(new URL[]{jar.getURL()});

				JarInputStream jis = new JarInputStream(jar.getContent().getInputStream());
				Manifest manifest = jis.getManifest();
				String className=null;
				if (manifest!=null){
					Attributes at = manifest.getMainAttributes();
					className = at.getValue("Application-Module");
				}

				if(className!=null && !className.isEmpty()){
					try{
						ApplicationModule module = (ApplicationModule) cloader.loadClass(className).newInstance();
						ApplicationModule older = context.getOlderModulePresent(module.getModuleID());
						if (older!=null){
							older.unload(context);
							//module.load(context);
						}
						context.addModule(module);
					} catch (ClassCastException e){
						log.warn(className + " is not a valid application module activator");
					}
				}else {
					log.warn(jar.getName() + " does not present an application module.");
				}

			}catch (IOException e) {
				ManagedIOException.manage(e);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}


	}





}
