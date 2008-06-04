package org.middleheaven.core.services.engine;

import org.middleheaven.core.services.ServiceContext;

public abstract class ServiceActivator {

	
	public abstract void activate(ServiceContext context);
	public abstract void inactivate(ServiceContext context);
	
}
