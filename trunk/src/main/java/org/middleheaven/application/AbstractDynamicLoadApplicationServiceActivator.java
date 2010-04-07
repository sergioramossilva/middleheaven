package org.middleheaven.application;

import org.middleheaven.core.bootstrap.BootstapListener;
import org.middleheaven.core.bootstrap.BootstrapEvent;
import org.middleheaven.core.bootstrap.BootstrapService;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.core.wiring.activation.ActivationContext;
import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;
import org.middleheaven.core.wiring.annotations.Wire;
import org.middleheaven.core.wiring.service.Service;
import org.middleheaven.logging.LogBook;
import org.middleheaven.logging.LoggingService;

/**
 * Provides a support implementation for {@link ApplicationLoadingService} enabling loading application 
 * modules present at the application configuration path
 * 
 * 
 */
public abstract class AbstractDynamicLoadApplicationServiceActivator extends Activator implements BootstapListener  {

	private LogBook log;

	private LoggingService loggingService;
	private BootstrapService bootstrapService;
	private ApplicationLoadingService applicationLoadingCycleService;
	private WiringService wiringService;

	private TransientApplicationContext appContext;
	private DefaultApplicationLoadingCycle cycle;
	
	public AbstractDynamicLoadApplicationServiceActivator(){}
	
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
		appContext = new TransientApplicationContext(wiringService.getObjectPool(),bootstrapService.getEnvironmentBootstrap().getContainer().getFileSystem());
		cycle = new DinamicLoadingCycle();
		
		applicationLoadingCycleService =  new DynamicLoadApplicationService();
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
	
	@Service
	private class DynamicLoadApplicationService implements ApplicationLoadingService {
		
		public DynamicLoadApplicationService() {	}
		
		@Override
		public ApplicationLoadingCycle getApplicationLoadingCycle() {
			return cycle;
		}
		
		@Override
		public ApplicationContext getApplicationContext(){
			return appContext;
		}
		
	}
	
	private class DinamicLoadingCycle extends DefaultApplicationLoadingCycle{

		public DinamicLoadingCycle() {
			super();
		}

		@Override
		public void start() {
	
			if (this.setState(ApplicationCycleState.STOPED)){
				log.info("Scanning for applications");
				loadPresentModules();
				log.info("Activating applications");
				for (ApplicationModule module : appContext.modules()){
					try {
						module.load(appContext);
					} catch (RuntimeException e){
						log.error(e  , "Impossible to activate {0}" , module.getModuleID());
						throw e;
					}
				}
				this.setState(ApplicationCycleState.LOADED);
				this.setState(ApplicationCycleState.READY);
				log.info("Applications ready");
			}
			
		}

		@Override
		public void stop() {
			this.setState(ApplicationCycleState.PAUSED);
			log.info("Deactivating applications");
			for (ApplicationModule module : appContext.modules()){
				try {
					module.unload(appContext);
				} catch (Exception e){
					log.warn(e,"Impossible to deactivate {0}", module.getModuleID());
				}
			}
			this.setState(ApplicationCycleState.STOPED);
			log.info("Applications deactivated");
		}

		
	}

	protected TransientApplicationContext getAppContext() {
		return appContext;
	}

	protected LogBook getLog() {
		return log;
	}

	protected BootstrapService getBootstrapService() {
		return bootstrapService;
	}

	protected WiringService getWiringService() {
		return wiringService;
	}

	protected DefaultApplicationLoadingCycle getCycle() {
		return cycle;
	}

	protected abstract void loadPresentModules();



}
