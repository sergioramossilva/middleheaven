/*
 * Created on 2006/08/19
 *
 */
package org.middleheaven.core.bootstrap;

import java.util.ArrayList;

import org.middleheaven.core.Container;
import org.middleheaven.core.ContextIdentifier;
import org.middleheaven.core.services.RegistryServiceContext;
import org.middleheaven.core.services.ServiceContextConfigurator;
import org.middleheaven.core.services.ServiceContextEngineConfigurationService;
import org.middleheaven.core.services.discover.ServiceActivatorDiscoveryEngine;
import org.middleheaven.core.services.engine.ActivatorBagServiceDiscoveryEngine;
import org.middleheaven.core.services.engine.LocalFileRepositoryDiscoveryEngine;
import org.middleheaven.core.wiring.DefaultWiringService;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.io.repository.FileRepositoryActivator;
import org.middleheaven.logging.LogBook;
import org.middleheaven.logging.LoggingActivator;

/**
 * This is the entry point for all applications
 * Subclasses of <code>ExecutionEnvironmentBootstrap</code> implements
 * bootstrap in different execution environments and allow
 * the applications execution to be environment independent.
 * 
 * @author Sergio M. M. Taborda 
 *
 */
public abstract class ExecutionEnvironmentBootstrap {

	
	private ListServiceContextConfigurator configurator = new ListServiceContextConfigurator();
	
	/**
	 * Start the environment 
	 */
	public final void start(LogBook log){
		long time = System.currentTimeMillis();
		
		RegistryServiceContext serviceRegistryContext = new RegistryServiceContext(log);

		doBeforeStart();

		log.debug("Resolving container");
		
		Container container = getContainer();
		
		log.trace("Container resolved: " + container.getEnvironmentName());
		
		log.debug("Register bootstrap services");
		
		serviceRegistryContext.register(WiringService.class, new DefaultWiringService(),null);
		serviceRegistryContext.register(BootstrapService.class, new SimpleBootstrapService(container),null);
		serviceRegistryContext.register(ServiceContextEngineConfigurationService.class, new UniqueServiceContextEngineConfigurationService(), null);
		
		
		log.debug("Inicialize service discovery engines");
		
		ActivatorBagServiceDiscoveryEngine engine = new ActivatorBagServiceDiscoveryEngine()
		.addActivator(LoggingActivator.class)
		.addActivator(FileRepositoryActivator.class);
		
		
		configurator.addEngine(engine);
		configurator.addEngine(new LocalFileRepositoryDiscoveryEngine());

		container.init(this);

		doAfterStart();

		log.info("Environment inicialized in " + (System.currentTimeMillis()-time) + " ms.");

	}

	
	public final void stop(){

		doBeforeStop();
		configurator.clear();
		doAfterStop();
	}

	protected void doBeforeStart(){};
	protected void doAfterStart(){}; 
	protected void doBeforeStop(){};
	protected void doAfterStop(){}; 

	public abstract ContextIdentifier getContextIdentifier();
	public abstract Container getContainer();

	private class ListServiceContextConfigurator extends ServiceContextConfigurator {
		
		ArrayList<ServiceActivatorDiscoveryEngine> engines = new ArrayList<ServiceActivatorDiscoveryEngine>();
		public void addEngine(ServiceActivatorDiscoveryEngine engine){
			engines.add(engine);
			engines.trimToSize();
			super.addEngine(engine);
		}
		
		public void clear() {
			for (ServiceActivatorDiscoveryEngine engine : engines){
				super.removeEngine(engine);
			}
		}
	}
	
		
	private class UniqueServiceContextEngineConfigurationService implements ServiceContextEngineConfigurationService{


		@Override
		public ServiceContextConfigurator getServiceContextConfigurator() {
			return configurator;
		}
		
	}
}
