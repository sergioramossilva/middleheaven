package org.middleheaven.core.bootstrap.activation;

import java.util.Collection;

import org.middleheaven.core.services.ServiceContext;


/**
 * Parent class of all service activators.
 * 
 * An activator must have a no arguments constructor and implement the methods in this class.
 * 
 * An activator should procure required services and publish its own services to the {@link ServiceContext}.
 * 
 */
public abstract class ServiceActivator {
	
	public ServiceActivator(){}
	
	
	public abstract void collectRequiredServicesSpecifications(Collection<ServiceSpecification> specs);
	
	public abstract void collectPublishedServicesSpecifications(Collection<ServiceSpecification> specs);
	
	/**
	 * Called to activate units. 
	 */
	public abstract void activate(ServiceContext serviceContext);
	
	/**
	 * Called to inactivity units. 
     * The activator must release resources, deregister services and free dependencies 
	 */
	public abstract void inactivate(ServiceContext serviceContext);
}
