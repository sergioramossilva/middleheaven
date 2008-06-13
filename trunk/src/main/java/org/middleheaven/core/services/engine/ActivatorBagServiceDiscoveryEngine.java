package org.middleheaven.core.services.engine;

import java.util.LinkedList;
import java.util.List;

import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.ServiceDiscoveryEngine;

public class ActivatorBagServiceDiscoveryEngine implements ServiceDiscoveryEngine {

	private List<ServiceActivator> activators = new LinkedList<ServiceActivator>();
	
	public ActivatorBagServiceDiscoveryEngine(){

	}

	public void addActivator(ServiceActivator activator){
		activators.add(activator);
	}

	@Override
	public void init(ServiceContext context) {
		
		for (ServiceActivator activator : activators){
			try {
				activator.activate(context);
			} catch (Exception e){
				// TODO log
			}
		}
		
		
	}

	@Override
	public void stop(ServiceContext context) {
		// no-op
		for (ServiceActivator activator : activators){
			try {
				activator.inactivate(context);
			} catch (Exception e){
				// TODO log
			}
		}
	}


}
