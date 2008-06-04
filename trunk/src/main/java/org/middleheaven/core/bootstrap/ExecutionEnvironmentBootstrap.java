/*
 * Created on 2006/08/19
 *
 */
package org.middleheaven.core.bootstrap;

import java.util.ArrayList;

import org.middleheaven.core.Container;
import org.middleheaven.core.ContainerActivator;
import org.middleheaven.core.ContextIdentifier;
import org.middleheaven.core.services.ServiceContextConfigurator;
import org.middleheaven.core.services.ServiceContextEngineConfigurationService;
import org.middleheaven.core.services.ServiceDiscoveryEngine;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.services.engine.ActivatorBagServiceDiscoveryEngine;
import org.middleheaven.core.services.engine.LocalFileRepositoryDiscoveryEngine;
import org.middleheaven.io.repository.FileRepositoryActivator;
import org.middleheaven.logging.LoggingActivator;

/**
 * This is the entry point for all applications
 * Subclasses of <code>ExecutionEnvironmentBootstrap</code> implements
 * bootstrap in different execution environments and allow
 * the applications execution to be environment independent.
 * 
 * The <code>ExecutionEnvironmentBootstrap</code> also starts the OSGi implementation service
 * @author Sergio M. M. Taborda 
 *
 */
public abstract class ExecutionEnvironmentBootstrap {

	
	private ListServiceContextConfigurator configurator = new ListServiceContextConfigurator();
	/**
	 * Entrypoint 
	 */
	public final void start(){
		long time = System.currentTimeMillis();
		doBeforeStart();

		Container container = getContainer();
		ServiceRegistry.register(ServiceContextEngineConfigurationService.class, new UniqueServiceContextEngineConfigurationService(), null);
		
		ActivatorBagServiceDiscoveryEngine engine = new ActivatorBagServiceDiscoveryEngine();
		
		// default init
		engine.addActivator(new ContainerActivator(container));
		engine.addActivator(new FileRepositoryActivator());
		engine.addActivator(new LoggingActivator());
		
		configurator.addEngine(engine);
		configurator.addEngine(new LocalFileRepositoryDiscoveryEngine());

		container.init(this);

		doAfterStart();

		System.out.println("Environment inicialized in " + (System.currentTimeMillis()-time) + " ms.");

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
		
		ArrayList<ServiceDiscoveryEngine> engines = new ArrayList<ServiceDiscoveryEngine>();
		public void addEngine(ServiceDiscoveryEngine engine){
			engines.add(engine);
			engines.trimToSize();
			super.addEngine(engine);
		}
		
		public void clear() {
			for (ServiceDiscoveryEngine engine : engines){
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
