package org.middleheaven.core.bootstrap;

/**
 * 
 */
public interface BootstrapService {

	
	public void addListener(BootstapListener listener);
	public void removeListener(BootstapListener listener);
	
	
	/**
	 * Stop the MiddleHeaven bootstrap.
	 */
	public void stop();
}
