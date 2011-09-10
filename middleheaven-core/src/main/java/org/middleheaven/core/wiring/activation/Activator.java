package org.middleheaven.core.wiring.activation;

/**
 * Parent class of all activators.
 * An activator must have a no arguments constructor and implement the methods in this class.
 * 
 */
public abstract class Activator {

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
