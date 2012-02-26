package org.middleheaven.core.wiring.activation;

import org.middleheaven.core.wiring.annotations.PostCreate;
import org.middleheaven.core.wiring.annotations.PreDestroy;

/**
 * Parent class of all activators.
 * An activator must have a no arguments constructor and implement the methods in this class.
 * 
 */
public abstract class Activator {

	/**
	 * Called to activate units. 
	 */
	@PostCreate
	public abstract void activate();
	
	/**
	 * Called to inactivity units. 
     * The activator must release resources, deregister services and free dependencies 
	 */
	@PreDestroy
	public abstract void inactivate();
}
