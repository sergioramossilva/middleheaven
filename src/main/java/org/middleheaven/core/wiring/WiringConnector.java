package org.middleheaven.core.wiring;

/**
 * Allows for interaction with different annotation specifications 
 * The connector may register on or more {@link WiringModelReader} with the binder.
 *  
 */
public interface WiringConnector {

	public void connect(ConnectableBinder binder);
	
}
