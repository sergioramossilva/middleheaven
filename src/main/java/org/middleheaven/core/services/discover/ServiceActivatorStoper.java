package org.middleheaven.core.services.discover;

import java.util.List;

import org.middleheaven.core.services.ServiceContext;

public class ServiceActivatorStoper {

	List<ServiceActivator> activators;
	ServiceContext context;
	
	public ServiceActivatorStoper(ServiceContext context,List<ServiceActivator> activators) {
		this.context = context;
		this.activators = activators;
	}

	public void stop() {
		for (ServiceActivator activator : this.activators){
			
			ServiceActivatorInfo info = new ServiceActivatorInfo((Class<ServiceActivator>)activator.getClass());
			
			info.unPublishServices(activator, context);
			
			activator.inactivate(context);
		
		}
		
	}

}
