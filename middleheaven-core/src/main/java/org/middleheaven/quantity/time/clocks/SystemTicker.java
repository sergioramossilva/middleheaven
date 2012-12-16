/**
 * 
 */
package org.middleheaven.quantity.time.clocks;

/**
 * 
 */
public class SystemTicker implements Ticker {

	
	private static SystemTicker ME = new SystemTicker();

	public static SystemTicker getInstance(){
		return ME;
	}
	
	private SystemTicker(){}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getTick() {
		return System.nanoTime();
	}

}
