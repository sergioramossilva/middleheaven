package org.middleheaven.core.wiring;

/**
 * Finds and retrieves an object compatible with the given {@link WiringSpecification}
 * 
 * @param <T> The type of object to retrieve
 */
public interface Resolver<T> {

	/**
	 * 
	 * @param specification 
	 * @return retrieves an object compatible with the specification
	 */
	public T resolve(WiringSpecification<T> specification);
}
