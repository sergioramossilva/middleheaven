package org.middleheaven.quantity.math.structure;

/**
 * 
 * Marks a class as belonging to a ring.
 *
 * @param <T>
 */
public interface Ring<T> extends GroupAdditive<T> {

	/**
	 * Multiplication
	 * @param other factor
	 * @return <code>this</code> times <code>other</code>
	 */
	public T times(T other);
	
	/**
	 * One is the ring neutral element defined as <code>this.times(one) = this</code>
	 * @return zero
	 */
	public T one();
}
