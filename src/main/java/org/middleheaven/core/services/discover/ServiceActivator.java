package org.middleheaven.core.services.discover;

import org.middleheaven.core.services.ServiceAtivatorContext;

/**
 * Controles the activation and deactivation of services. 
 * The activator can request dependencies by implementing modifers (set) methods annotated with <code>Require</code> annotation
 * The activator can public services by implementing accessor (get) methods annotated with <code>Publish</code> annotation
 * 
 * The required and publish services will be used to determine dependencies witch will be automatically resolve at runtime.
 * 
 * @author Sérgio Taborda
 *
 */
public abstract class ServiceActivator {

	/**
	 * Called to activate services. The ServiceActivator can initialize services and resources.
	 * @param context
	 */
	public abstract void activate(ServiceAtivatorContext context);
	
	/**
	 * Called to deactivate services. The ServiceActivator can close all resources.
	 * @param context
	 */
	public abstract void inactivate(ServiceAtivatorContext context);
	
}
