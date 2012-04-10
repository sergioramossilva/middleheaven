package org.middleheaven.core.bootstrap.activation;


/**
 * Searches for a set of {@link ServiceActivator}s.
 * 
 * The found activators can come and go, so events are trigged when this happens.
 * 
 * The {@link ActivatorScanner#scan()} method starts the scan process for the first time. 
 */
public interface ActivatorScanner {

	/**
	 * Adds an {@link ActivatorScannerListener} to the this <code>ActivatorScanner</code>.
	 * @param listener the listener to add.
	 */
	public void addScannerListener(ActivatorScannerListener listener);
	
	/**
	 * Removes an {@link ActivatorScannerListener} from this <code>ActivatorScanner</code>.
	 * @param listener the listener to remove.
	 */
	public void removeScannerListener(ActivatorScannerListener listener);

	/**
	 * Scans for activators.
	 */
	public void scan();
}
