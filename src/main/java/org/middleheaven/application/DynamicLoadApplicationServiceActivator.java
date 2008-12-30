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
import org.middleheaven.core.services.Publish;
import org.middleheaven.core.services.Require;
import org.middleheaven.core.services.ServiceAtivatorContext;
import org.middleheaven.core.services.discover.ServiceActivator;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.FileChangeEvent;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileFilter;
import org.middleheaven.io.repository.WatchableRepository;
import org.middleheaven.logging.LogBook;
import org.middleheaven.logging.LoggingService;

public class DynamicLoadApplicationServiceActivator extends ServiceActivator implements BootstapListener  {


	private LogBook log;

	private LoggingService loggingService;
	private BootstrapService bootstrapService;
	private ApplicationLoadingService applicationLoadingCycleService;

	private WiringService wiringService;
	
	@Require
	public void setBootstrapService(BootstrapService bootstrapService) {
		this.bootstrapService = bootstrapService;
	}

	@Require
	public void setLoggingService(LoggingService loggingService) {
		this.loggingService = loggingService;
	}
	
	@Require
	public void setWiringService(WiringService wiringService) {
		this.wiringService = wiringService;
	}
	
	@Publish
	public ApplicationLoadingService getApplicationLoadingCycleService() {
		return applicationLoadingCycleService;
	}

	@Override
	public void activate(ServiceAtivatorContext context) {
		log = loggingService.getLogBook(this.getClass().getName());
	
		bootstrapService.addListener(this);
		applicationLoadingCycleService =  new DynamicLoadApplicationService( bootstrapService.getEnvironmentBootstrap().getContainer());
	}

	
	public DynamicLoadApplicationServiceActivator(){}


	public static void setAppModulesFilter(ManagedFileFilter appModulesFilter) {
		DynamicLoadApplicationServiceActivator.appModulesFilter = appModulesFilter;
	}

	private static ManagedFileFilter appModulesFilter =  new ManagedFileFilter(){

		@Override
		public Boolean classify(ManagedFile file) {
			return file.getName().endsWith(".apm");
		}
	};


	@Override
	public void inactivate(ServiceAtivatorContext context) {
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
			context = new TransientApplicationContext(wiringService.getWiringContext(),container);
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
				WatchableRepository wr = (WatchableRepository)f;
				wr.addFileChangelistener(cycle, f);
			}

			// filter apm only (apm are jar files)
			applicationModuleFiles.addAll( f.listFiles(appModulesFilter));


			URLClassLoader cloader ;
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
