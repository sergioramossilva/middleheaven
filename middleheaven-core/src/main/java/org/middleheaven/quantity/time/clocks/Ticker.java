/**
 * 
 */
package org.middleheaven.quantity.time.clocks;

/**
 * Represents a relative time elapsing device similiar in mechanics as {@link System#nanoTime()}.
 */
public interface Ticker {

	/**
	 * Time tick in nanoseconds.
	 * @return Time tick in nanoseconds.
	 */
	public long getTick();
}
