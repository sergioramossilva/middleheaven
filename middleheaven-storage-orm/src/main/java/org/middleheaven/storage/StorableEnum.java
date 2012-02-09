package org.middleheaven.storage;

/**
 * Marks an enumeration that can be stored.
 */
public interface StorableEnum<T> {

	/**
	 * 
	 * @return identity value 
	 */
	public T getStorableValue();

	/**
	 * 
	 * @return identity value 
	 */
	public Class<T> getStorableClass();
}
