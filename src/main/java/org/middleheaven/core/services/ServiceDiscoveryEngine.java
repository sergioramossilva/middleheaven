package org.middleheaven.core.services;

import org.middleheaven.core.services.engine.ServiceActivator;


public interface ServiceDiscoveryEngine {


	public void init(ServiceContext context);

	public void stop();
	
	
}
