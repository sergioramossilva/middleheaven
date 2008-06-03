package org.middleheaven.util.measure.time;

import org.middleheaven.util.measure.Quantity;

/**
 * Represents an amount of time elapsed from an epoch
 * The epoch used is January 1 , 1970 at 00:00:00 hours
 *
 */
public abstract class ElapsedTime implements Quantity{

	
	public abstract ElapsedTime negate();
}
