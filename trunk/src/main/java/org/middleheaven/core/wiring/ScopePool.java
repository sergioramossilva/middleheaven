package org.middleheaven.core.wiring;

/**
 * Interface for all pools that belong to a scope.
 * scoped objects are mantained in pools. 
 * These pools are abstract in the sense they may be empty in reality.
 * If the pool does not contain the object it requests it to the passed resolver. 
 */
public interface ScopePool {

	
	public <T> T scope (WiringSpecification<T> query, Resolver<T> resolver);
}
