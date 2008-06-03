package org.middleheaven.util.measure.structure;

/**
 * Marks a class as belonging to a ring
 * and have inverse capabilities.
 * 
 *
 * @param <T>
 */
public interface GroupMultiplicative<T>  {


	public T over(T other);
	public T inverse();
}
