package org.middleheaven.core.wiring;

/**
 * Allows for interaction with different annotation specifications 
 * The connector may register one or more {@link WiringModelReader} with the binder.
 *  
 */
public interface WiringConnector {

	/**
	 * Connect to the specified binder. Normally the connector will register 
	 * one or more {@link WiringModelReader}  with the binder.
	 * @param binder
	 */
	public void connect(ConnectableBinder binder);
	
}
