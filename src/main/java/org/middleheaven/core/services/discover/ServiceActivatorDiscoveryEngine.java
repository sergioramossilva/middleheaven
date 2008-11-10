package org.middleheaven.core.services.discover;

import java.util.LinkedList;
import java.util.List;

import org.middleheaven.core.dependency.DependencyResolver;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.logging.LogBook;


/**
 * A strategy pattern common interface for loading services implementations.
 * 
 * 1: discover all activators present
 */
public abstract class ServiceActivatorDiscoveryEngine implements ServiceDiscoveryEngine {

	private final List<ServiceActivator> activators = new LinkedList<ServiceActivator>();
	
	public void init(ServiceContext context) {
		LogBook log = context.getLogBook();
	    List<ServiceActivatorInfo> activatorsInfo = discoverActivators(context);
		
	    ServiceActivatorStarter starter = new ServiceActivatorStarter(context,activators);
	    
	    new DependencyResolver(context.getLogBook()).resolve(activatorsInfo, starter);
	    
	}

	public void stop(ServiceContext context) {
		LogBook log = context.getLogBook();
		
		for (ServiceActivator activator : this.activators){
			try {
				activator.inactivate(context);
			} catch (Exception e){
				log.fatal("Impossible to  inactivate " + activator.getClass().getName(), e);
			}
		}
	}
	
	protected abstract List<ServiceActivatorInfo>  discoverActivators(ServiceContext context);
}
