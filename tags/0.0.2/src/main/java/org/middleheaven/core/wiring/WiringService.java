package org.middleheaven.core.wiring;

import org.middleheaven.core.wiring.activation.ActivatorScanner;

/**
 * Wire Service 
 * Provides access to the current <code>WiringContext</code>
 */
public interface WiringService {

	
	public ObjectPool getObjectPool();
	public void addConnector(WiringConnector ... connectors); 
	
	public void addActivatorScanner(ActivatorScanner scanner);
	public void removeActivatorScanner(ActivatorScanner scanner);
	public void scan();
}
