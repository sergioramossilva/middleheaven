package org.middleheaven.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.middleheaven.core.Container;
import org.middleheaven.core.bootstrap.BootstapListener;
import org.middleheaven.core.bootstrap.BootstrapEvent;
import org.middleheaven.core.bootstrap.BootstrapService;
import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.core.services.Publish;
import org.middleheaven.core.services.Require;
import org.middleheaven.core.services.ServiceAtivatorContext;
import org.middleheaven.core.services.discover.ServiceActivator;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.FileChangeEvent;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileFilter;
import org.middleheaven.logging.LogBook;
import org.middleheaven.logging.LoggingService;

/**
 * Provides an {@link ApplicationLoadingService} for loading application modules present at the application configuration path
 * Searches for a  a manifest file
 * with an {@code Application-Module} entry pointing to the {@link ApplicationModule} class.
 * 
 * 
 */
public class MetaInfApplicationServiceActivator extends ServiceActivator implements BootstapListener  {


	private LogBook log;

	private LoggingService loggingService;
	private BootstrapService bootstrapService;
	private ApplicationLoadingService applicationLoadingCycleService;

	private WiringService wiringService;
	
	public static void setAppModulesFilter(ManagedFileFilter appModulesFilter) {
		MetaInfApplicationServiceActivator.appModulesFilter = appModulesFilter;
	}

	private static ManagedFileFilter appModulesFilter =  new ManagedFileFilter(){

		@Override
		public Boolean classify(ManagedFile file) {
			return file.getName().endsWith(".apm");
		}
	};
	
	public MetaInfApplicationServiceActivator(){}
	
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
			//no-op
		}
		

		protected void loadPresentModules() {

			ManagedFile f =  bootstrapService.getEnvironmentBootstrap().getContainer().getAppConfigRepository();

			ManagedFile manifest = f.resolveFile("MANIFEST.MF");
			
			if (manifest.exists()){
				BufferedReader reader = new BufferedReader(new InputStreamReader(manifest.getContent().getInputStream()));
				String line;
				try {
					while (((line = reader.readLine())!=null)){
						if (line.startsWith("Application-Module")){
	
							String className = line.substring(line.indexOf(":")+1).trim();
							if(className!=null && !className.isEmpty()){
								try{
									ApplicationModule module =  ReflectionUtils.newInstance(className, ApplicationModule.class);
									wiringService.getWiringContext().wireMembers(module);
									ApplicationModule older = context.getOlderModulePresent(module.getModuleID());
									if (older!=null){
										older.unload(context);
									}
									context.addModule(module);
								} catch (ClassCastException e){
									log.warn(className + " is not a valid application module activator");
								}
							}
							break;
						}
					}
				} catch (IOException e) {
					throw ManagedIOException.manage(e);
				}
			}

		}
	}
}
