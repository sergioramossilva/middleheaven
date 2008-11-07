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
import org.middleheaven.core.services.ContainerService;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.discover.ServiceActivator;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.FileChangeEvent;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileFilter;
import org.middleheaven.io.repository.WatchableRepository;
import org.middleheaven.logging.LogBook;
import org.middleheaven.logging.LoggingService;

public class DynamicLoadApplicationServiceActivator extends ServiceActivator  {


	private LogBook log;
	Container container;

	public DynamicLoadApplicationServiceActivator(){}

	private static ManagedFileFilter appModulesFilter =  new ManagedFileFilter(){

		@Override
		public Boolean classify(ManagedFile file) {
			return file.getName().endsWith(".apm");
		}
	};

	
	@Override
	public void activate(ServiceContext context) {
		log = context.getService(LoggingService.class, null).getLogBook(this.getClass().getName());
		container  = context.getService(ContainerService.class, null).getContainer();

		context.register(ApplicationLoadingCycleService.class, new DynamicLoadApplicationService(container), null);
	}

	@Override
	public void inactivate(ServiceContext context) {
		cycle.stop();
		log=null;
	}
	
	
	private DefaultApplicationLoadingCycle cycle;
	
	private class DynamicLoadApplicationService implements ApplicationLoadingCycleService {
		
		public DynamicLoadApplicationService(Container container) {
			TransientApplicationContext context = new TransientApplicationContext(container);
			cycle = new DinamicLoadingCycle(context);
		}
		@Override
		public ApplicationLoadingCycle getApplicationLoadingCycle() {
			return cycle;
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
			
			this.setState(ApplicationCycleState.STOPED);
			loadPresentModules();
			for (ApplicationModule module : context.modules()){
				try {
					module.load(context);
				} catch (Exception e){
					log.logWarn("Impossible to activate " + module.getModuleID(), e);
				}
			}
			this.setState(ApplicationCycleState.LOADED);
			this.setState(ApplicationCycleState.READY);
		}

		@Override
		public void stop() {
			this.setState(ApplicationCycleState.PAUSED);
			for (ApplicationModule module : context.modules()){
				try {
					module.unload(context);
				} catch (Exception e){
					log.logWarn("Impossible to deactivate " + module.getModuleID(), e);
				}
			}
			this.setState(ApplicationCycleState.STOPED);
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

			// look for all files ending in .apm
			// these must be  jar files
			Collection<ManagedFile> applicationModuleFiles = new HashSet<ManagedFile>();

			ManagedFile f = container.getAppConfigRepository();

			if (f.isWatchable()){
				WatchableRepository wr = (WatchableRepository)f;
				wr.addFileChangelistener(cycle, f);
			}

			// filter apm only 
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
						log.logWarn(className + " is not a valid application module activator");
					}
				}else {
					log.logWarn(jar.getName() + " does not present an application module.");
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


	};


}
