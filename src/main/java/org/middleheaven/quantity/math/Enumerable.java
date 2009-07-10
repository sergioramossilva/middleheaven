package org.middleheaven.quantity.math;

/**
 * Mark a value class as Enumerable. Enumerable are ordable 
 * and discreet, so given any value its possible to determine the next and the previous value.
 * 
 * @see org.middleheaven.quantity.math.BigInt
 *
 * @param <N> the real type.  
 */
public interface Enumerable<N> {
	
	public N previous();
	
	public N next();
}
