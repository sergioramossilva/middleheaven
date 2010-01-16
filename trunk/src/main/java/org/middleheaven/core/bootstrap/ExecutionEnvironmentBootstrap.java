/*
 * Created on 2006/08/19
 *
 */
package org.middleheaven.core.bootstrap;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.middleheaven.core.BootstrapContainer;
import org.middleheaven.core.services.RegistryServiceContext;
import org.middleheaven.core.wiring.DefaultWiringService;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.core.wiring.activation.ActivatorScanner;
import org.middleheaven.core.wiring.activation.FileActivatorScanner;
import org.middleheaven.core.wiring.activation.SetActivatorScanner;
import org.middleheaven.global.atlas.modules.AtlasActivator;
import org.middleheaven.global.text.LocalizationServiceActivator;
import org.middleheaven.io.repository.FileRepositoryActivator;
import org.middleheaven.logging.LogBook;
import org.middleheaven.logging.LoggingActivator;
import org.middleheaven.quantity.unit.SI;
import org.middleheaven.util.StopWatch;
import org.middleheaven.work.scheduled.AlarmClockScheduleWorkExecutionServiceActivator;

/**
 * This is the entry point for all applications
 * Subclasses of <code>ExecutionEnvironmentBootstrap</code> implement
 * bootstrap in different execution environments and allow
 * for the application's execution to be environment independent.
 * 
 *
 */
public abstract class ExecutionEnvironmentBootstrap {

	private SimpleBootstrapService bootstrapService;
	private RegistryServiceContext serviceRegistryContext;
	
	/**
	 * Start the environment 
	 */
	public final void start(LogBook log){
		bootstrapService = new SimpleBootstrapService(this);
		
		StopWatch watch = StopWatch.start();

		serviceRegistryContext = new RegistryServiceContext(log);

		doBeforeStart();

		log.trace("Resolving container");
		
		BootstrapContainer container = getContainer();
		
		log.info("Container resolved: " + container.getEnvironmentName());
		
		log.debug("Register bootstrap services");

		WiringService wiringService = new DefaultWiringService();
		serviceRegistryContext.register(WiringService.class, wiringService,null);
		serviceRegistryContext.register(BootstrapService.class, bootstrapService,null);

		doEnvironmentServiceRegistry(wiringService);
		
		bootstrapService.fireBootupStart();
		
		log.debug("Inicialize service discovery engines");
		
		// default scanner
		ActivatorScanner scanner = new SetActivatorScanner()
		.addActivator(AtlasActivator.class)
		.addActivator(LoggingActivator.class)
		.addActivator(FileRepositoryActivator.class);

		wiringService.addActivatorScanner(scanner);
		
		// file aware scannaer
		ActivatorScanner fileScanner = new FileActivatorScanner(container.getAppClasspathRepository(), ".jar$");
		wiringService.addActivatorScanner(fileScanner);

		// call configuration
		configurate(wiringService);
		
		// call container configuration
		container.init(wiringService);

		// activate services that can be overrriden by the container or final environment
		SetActivatorScanner overrridableScanner = new SetActivatorScanner()
		.addActivator(AlarmClockScheduleWorkExecutionServiceActivator.class)
		.addActivator(LocalizationServiceActivator.class);

		wiringService.addActivatorScanner(overrridableScanner);
		
		// can and activate all
		wiringService.scan();
		
		doAfterStart();
		
		container.start();

		log.info("Environment inicialized in " + watch.mark().asMeasure().convertTo(SI.MILI(SI.SECOND)).toString() + ".");
		bootstrapService.fireBootupEnd();
	}

	protected void doEnvironmentServiceRegistry(WiringService wiringService) {
		// no-op
	}

	public void configurate(WiringService wiringService){
		
	}

	private class SimpleBootstrapService implements BootstrapService{
		
		private ExecutionEnvironmentBootstrap executionEnvironmentBootstrap;
		private List<BootstapListener> listeners = new CopyOnWriteArrayList<BootstapListener>();
		
		public SimpleBootstrapService(ExecutionEnvironmentBootstrap executionEnvironmentBootstrap) {
			this.executionEnvironmentBootstrap = executionEnvironmentBootstrap;
		}

		protected void fireBootupStart() {
			fire (new BootstrapEvent(true,true));
		}
		
		protected void fireBootupEnd() {
			fire (new BootstrapEvent(true,false));
		}

		protected void fireBootdownEnd() {
			fire (new BootstrapEvent(false,false));
		}

		protected void fireBootdownStart() {
			fire (new BootstrapEvent(false,true));
		}
		
		protected void fire(BootstrapEvent event) {
			for (BootstapListener listener : listeners){
				listener.onBoostapEvent(event);
			}
		}
		

		@Override
		public void addListener(BootstapListener listener) {
			listeners.add(listener);
		}

		@Override
		public void removeListener(BootstapListener listener) {
			listeners.remove(listener);
		}

		@Override
		public ExecutionEnvironmentBootstrap getEnvironmentBootstrap() {
			return executionEnvironmentBootstrap;
		}
	
	}
	
	public final void stop(){
		bootstrapService.fireBootdownStart();
		doBeforeStop();
	
		serviceRegistryContext.unRegister(BootstrapService.class);
		serviceRegistryContext.unRegister(WiringService.class);
	
		doAfterStop();

		bootstrapService.fireBootdownEnd();
	}

	protected void doBeforeStart(){};
	protected void doAfterStart(){}; 
	protected void doBeforeStop(){};
	protected void doAfterStop(){}; 

	public abstract BootstrapContainer getContainer();


	
		

}
