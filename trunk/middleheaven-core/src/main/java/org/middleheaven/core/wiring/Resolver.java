package org.middleheaven.core.wiring;

/**
 * Finds and retrieves an object compatible with the given {@link WiringSpecification}
 * 
 * @param <T> The type of object to retrieve
 */
public interface Resolver {

	/**
	 * 
	 * @param context the resolution context
	 * @param query 
	 * @return retrieves an object compatible with the specification
	 */
	public Object resolve(ResolutionContext context, WiringQuery query);
}
