package org.middleheaven.core.bootstrap;

import org.middleheaven.core.services.ServiceActivator;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.wiring.ProfilesBag;
import org.middleheaven.core.wiring.PropertyManagers;

/**
 * Context of information used during bootstrap.
 */
public interface BootstrapContext {

	public BootstrapContext addActivator(Class<? extends ServiceActivator> type);
	
	public BootstrapContext removeActivator(Class<? extends ServiceActivator> type);
	
	public boolean contains(Class<? extends ServiceActivator> type);
	
	public String getName();

	public ProfilesBag getActiveProfiles();
	
	public ServiceContext getServiceContext();
	
	/**
	 * A {@link PropertyManagers} that provide acess to system and environment properties.
	 * @return {@link PropertyManagers} that provide acess to system and environment properties.
	 */
	public PropertyManagers getPropertyManagers();
	
}
