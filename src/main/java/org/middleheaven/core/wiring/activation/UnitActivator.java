package org.middleheaven.core.wiring.activation;


public abstract class UnitActivator {

	/**
	 * Called to activate units. 
	 * @param context
	 */
	public abstract void activate(ActivationContext context);
	
	/**
	 * Called to inactivate units. 
     * The activator must release resources, deregister services and free dependencies 
	 * @param context
	 */
	public abstract void inactivate(ActivationContext context);
}
