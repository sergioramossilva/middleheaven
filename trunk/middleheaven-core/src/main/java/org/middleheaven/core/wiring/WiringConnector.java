package org.middleheaven.core.wiring;

/**
 * Allows for further configuration of the binder.
 * 
 * The connector may register or unregister one or more {@link WiringModelReader} with the binder.
 * The connector may register or unregister one or more {@link WiringInterceptor} with the binder. 
 */
public interface WiringConnector {

	/**
	 * Connect to the specified binder. Normally the connector will register 
	 * one or more {@link WiringModelReader}s or one or more {@link WiringInterceptor}  with the binder.
	 * 
	 * @param binder the current binder.
	 */
	public void connect(ConnectableBinder binder);
	
	
}
