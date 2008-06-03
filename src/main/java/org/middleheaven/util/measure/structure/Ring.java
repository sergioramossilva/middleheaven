package org.middleheaven.util.measure.structure;

/**
 * 
 * Marks a class as belonging to a ring.
 *
 * @param <T>
 */
public interface Ring<T> extends GroupAdditive<T> {

	public T times(T other);
	
	/**
	 * One is defined as this.times(one) = this
	 * @return zero
	 */
	public T one();
}
