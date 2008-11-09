package org.middleheaven.core.services.discover;

import java.util.LinkedList;
import java.util.List;

import org.middleheaven.core.dependency.DependencyResolver;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.wiring.WiringContext;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.logging.LogBook;
import org.middleheaven.logging.LoggingService;


/**
 * A strategy pattern common interface for loading services implementations.
 * 
 * 1: discover all activators present
 */
public abstract class ServiceActivatorDiscoveryEngine implements ServiceDiscoveryEngine {

	private final List<ServiceActivator> activators = new LinkedList<ServiceActivator>();
	
	public void init(ServiceContext context) {
		
	    List<ServiceActivatorInfo> activatorsInfo = discoverActivators(context);
		
	    WiringContext wiringContext = context.getService(WiringService.class, null).getWiringContext();
	    
	    ServiceActivatorStarter starter = new ServiceActivatorStarter(context,activators);
	    
	    new DependencyResolver().resolve(wiringContext,activatorsInfo, starter);
	    
	}

	public void stop(ServiceContext context) {
		LogBook log = context.getService(LoggingService.class, null).getLogBook(this.getClass().getName());
		
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
