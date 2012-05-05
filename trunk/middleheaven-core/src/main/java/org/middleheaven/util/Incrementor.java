package org.middleheaven.util;

/**
 * Contains the rule to increment a value of type <code>T</code> to the next 
 * @param <T> the type to increment.
 */
public interface Incrementor<T> {

	/**
	 * Returns object incremented by an amount.
	 * @param object the object to increment.
	 * @return object incremented by an amount.
	 */
	public T increment(T object);
}
