package org.middleheaven.core.bootstrap;

import org.middleheaven.core.bootstrap.activation.ServiceActivator;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.wiring.ProfilesBag;

public interface ExecutionContext {

	public ExecutionContext addActivator(Class<? extends ServiceActivator> type);
	
	public ExecutionContext removeActivator(Class<? extends ServiceActivator> type);
	
	public boolean contains(Class<? extends ServiceActivator> type);
	
	public String getName();

	public ProfilesBag getActiveProfiles();
	
	public ServiceContext getServiceContext();
	
}
