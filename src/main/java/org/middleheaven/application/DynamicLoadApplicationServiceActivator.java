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
import org.middleheaven.core.services.engine.ServiceActivator;
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

	private static ManagedFileFilter appModulesFilter =  new ManagedFileFilter(){

		@Override
		public Boolean classify(ManagedFile file) {
			return file.getName().endsWith(".apm");
		}

	};

	TransientApplicationContext context;

	private DefaultApplicationLoadingCycle cycle = new DefaultApplicationLoadingCycle(){

		@Override
		public void start() {
			context = new TransientApplicationContext();
			this.setState(ApplicationCycleState.STOPED);
			loadPresentModules();
			this.setState(ApplicationCycleState.LOADED);
			// TODO validade context

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

	};

	@Override
	public void activate(ServiceContext context) {
		log = context.getService(LoggingService.class, null).getLogBook(this.getClass().getName());
		container  = context.getService(ContainerService.class, null).getContainer();

		context.register(ApplicationLoadingCycleService.class, new DynamicLoadApplicationService(), null);
	}

	protected void loadPresentModules(){


		Collection<ManagedFile> serviceJars = new HashSet<ManagedFile>();

		ManagedFile f = container.getAppConfigRepository();

		if (f.isWatchable()){
			WatchableRepository wr = (WatchableRepository)f;
			wr.addFileChangelistener(cycle, f);
		}

		// filter apm only 
		serviceJars.addAll( f.listFiles(appModulesFilter));


		URLClassLoader cloader ;
		for (ManagedFile jar : serviceJars){

			loadModuleFromFile(jar);

		}

	}


	private void loadModuleFromFile(ManagedFile jar) {

		try{
			URLClassLoader cloader = URLClassLoader.newInstance(new URL[]{jar.getURL()});

			JarInputStream jis = new JarInputStream(jar.getContent().getInputStream());
			Manifest manifest = jis.getManifest();
			Attributes at = manifest.getMainAttributes();

			String className = at.getValue("Application-Module");

			if(className!=null && !className.isEmpty()){
				try{
					ApplicationModule module = (ApplicationModule) cloader.loadClass(className).newInstance();
					ApplicationModule older = context.getOlderModulePresent(module.getModuleID());
					if (older!=null){
						older.unload(context);
					}
					module.load(context);

				} catch (ClassCastException e){
					log.logWarn(className + " is not a valid application module activator");
				}
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

	@Override
	public void inactivate(ServiceContext context) {
		cycle.stop();
		log=null;
	}

	private class DynamicLoadApplicationService implements ApplicationLoadingCycleService {

		@Override
		public ApplicationLoadingCycle getApplicationLoadingCycle() {
			return cycle;
		}

	}




}
