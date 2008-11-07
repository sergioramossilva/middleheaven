package org.middleheaven.core.services.discover;

import org.middleheaven.core.services.ServiceContext;

public interface ServiceDiscoveryEngine {

	public void init(ServiceContext context);
	
	public void stop(ServiceContext context);
}
