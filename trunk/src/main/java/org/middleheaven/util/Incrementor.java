package org.middleheaven.util;

public interface Incrementor<T> {

	/**
	 * Returns object incremented by an amount.
	 * @param object
	 * @return object incremented by an amount.
	 */
	public T increment(T object);
}
