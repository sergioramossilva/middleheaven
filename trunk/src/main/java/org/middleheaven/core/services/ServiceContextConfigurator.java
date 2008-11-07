package org.middleheaven.core.services;

import org.middleheaven.core.services.discover.ServiceDiscoveryEngine;


public class ServiceContextConfigurator {

	
	public void addEngine(ServiceDiscoveryEngine engine){
	
		ServiceRegistry.addEngine(engine);
	}
	
	public void removeEngine(ServiceDiscoveryEngine engine){
		ServiceRegistry.removeEngine(engine);
	}
	

}
