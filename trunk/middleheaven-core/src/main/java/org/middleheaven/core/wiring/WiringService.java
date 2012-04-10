package org.middleheaven.core.wiring;

import org.middleheaven.core.wiring.activation.ActivatorScanner;

/**
 * Wire Service.
 * 
 * 
 */
public interface WiringService {

	/**
	 * The pool of candidate objects for wiring in other objects. 
	 * the {@link ObjectPool} provides access to objects in the context an to operations for wiring other objects.
	 * 
	 * @return The pool of candidate objects. 
	 */
	public ObjectPool getObjectPool();
	
	/**
	 * Adds one or more {@link WiringConnector}s to the service. 
	 * 
	 * This allows for further configuration of the binder.
	 * 
	 * @see ConnectableBinder
	 * @param connectors an array of {@link WiringConnector}.
	 */
	public void addConnector(WiringConnector ... connectors); 
	

	

}