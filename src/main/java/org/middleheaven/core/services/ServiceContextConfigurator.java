package org.middleheaven.core.services;


public class ServiceContextConfigurator {

	
	public void addEngine(ServiceDiscoveryEngine engine){
	
		ServiceRegistry.addEngine(engine);
	}
	
	public void removeEngine(ServiceDiscoveryEngine engine){
		ServiceRegistry.removeEngine(engine);
	}
	

}
