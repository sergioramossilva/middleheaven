/*
 * Created on 2006/08/19
 *
 */
package org.middleheaven.core.bootstrap;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.middleheaven.core.services.RegistryServiceContext;
import org.middleheaven.core.wiring.DefaultWiringService;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.ActivatorScanner;
import org.middleheaven.core.wiring.activation.FileActivatorScanner;
import org.middleheaven.core.wiring.activation.SetActivatorScanner;
import org.middleheaven.global.atlas.modules.AtlasActivator;
import org.middleheaven.global.text.LocalizationServiceActivator;
import org.middleheaven.io.repository.FileRepositoryActivator;
import org.middleheaven.logging.LogBook;
import org.middleheaven.logging.LoggingActivator;
import org.middleheaven.mail.service.MailBootstrapExtention;
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

		BootstrapContainer container = getContainer();
		ContainerFileSystem fileSystem = container.getFileSystem();
		
		log.info("Using {0} container", container.getContainerName());
		
	
		log.debug("Register bootstrap services");

		WiringService wiringService = new DefaultWiringService();
		serviceRegistryContext.register(WiringService.class, wiringService,null);
		serviceRegistryContext.register(BootstrapService.class, bootstrapService,null);

		// set scanner
		final SetActivatorScanner scanner = new SetActivatorScanner()
		.addActivator(AtlasActivator.class)
		.addActivator(LoggingActivator.class)
		.addActivator(FileRepositoryActivator.class);

	
		BootstrapContext context = new BootstrapContext(){

			@Override
			public BootstrapContext addActivator(Class<? extends Activator> activatorType) {
				scanner.addActivator(activatorType);
				return this;
			}

			@Override
			public BootstrapContext removeActivator(Class<? extends Activator> activatorType) {
				scanner.removeActivator(activatorType);
				return this;
			}

			@Override
			public boolean contains(Class<? extends Activator> activatorType) {
				return scanner.contains(activatorType);
			}
			
		};
		
		preConfig(context);
		
		bootstrapService.fireBootupStart();
		
		log.debug("Inicialize service discovery engines");
		
		
		// file aware scannaer
		ActivatorScanner fileScanner = new FileActivatorScanner(fileSystem.getAppClasspathRepository(), ".jar$");
		wiringService.addActivatorScanner(fileScanner);
		
		log.debug("Scanning for extentions");
		
		List<BootstrapContainerExtention> extentions = new ArrayList<BootstrapContainerExtention>();
		readExtentions(extentions);
		
		// configurate container with chain of extentions
		BootstrapChain chain = new BootstrapChain(extentions,container);
		
		chain.doChain(context);
		
		// call configuration
		posConfig(context);

		
		// activate services that can be overrriden by the container or final environment
	
		scanner.addActivator(AlarmClockScheduleWorkExecutionServiceActivator.class)
		.addActivator(LocalizationServiceActivator.class);

		wiringService.addActivatorScanner(scanner);
		
		// scan and activate all
		wiringService.scan(); 
		
		doAfterStart();
		
		container.start();

		log.info("Environment inicialized in {0}. " , watch.mark());
		bootstrapService.fireBootupEnd();
	}

	
	private void readExtentions(List<BootstrapContainerExtention> extentions){
		extentions.add(new MailBootstrapExtention());
	}
	
	protected void preConfig(BootstrapContext contex) {
		// no-op
	}

	public void posConfig(BootstrapContext context){
		
	}

	private class SimpleBootstrapService implements BootstrapService{
		
		private ExecutionEnvironmentBootstrap executionEnvironmentBootstrap;
		private Set<BootstapListener> listeners = new CopyOnWriteArraySet<BootstapListener>();
		
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
