package org.middleheaven.core.wiring;

/**
 * Allows for interaction with different annotation specifications 
 * The connector may register on or more {@link WiringModelParser} with the binder.
 *  
 */
public interface WiringConnector {

	public void connect(ConnectableBinder binder);
	
}
