package org.middleheaven.core.wiring;

/**
 * Wire Service 
 * Provides access to the current <code>WiringContext</code>
 */
public interface WiringService {

	
	public WiringContext getWiringContext();
	public void addConnector(WiringConnector ... connectors); 
}
