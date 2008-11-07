package org.middleheaven.core.services.discover;

import java.util.List;

import org.middleheaven.core.dependency.DependencyLoader;
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

	private  List<ServiceActivator> activators;
	
	public void init(ServiceContext context) {
		
	    //LogBook log = context.getService(LoggingService.class, null).getLogBook(this.getClass().getName());
		
	    List<Class<ServiceActivator>> activatorsClasses = discoverActivators(context);
		
	    WiringContext wiringContext = context.getService(WiringService.class, null).getWiringContext();
	    
	    DependencyLoader loader = new DependencyLoader();
	    
	    ServiceActivatorStarter<ServiceActivator> starter = new ServiceActivatorStarter<ServiceActivator>(context,activators);
	    
	    loader.load(wiringContext,activatorsClasses, starter);
	    
	}

	public void stop(ServiceContext context) {
		LogBook log = context.getService(LoggingService.class, null).getLogBook(this.getClass().getName());
		
		for (ServiceActivator activator : this.activators){
			try {
				activator.inactivate(context);
			} catch (Exception e){
				log.logFatal("Impossible to  inactivate " + activator.getClass().getName(), e);
			}
		}
	}
	
	protected abstract List<Class<ServiceActivator>>  discoverActivators(ServiceContext context);
}
