package org.middleheaven.core.wiring;

/**
 * Interface for all pools that belong to a scope.
 * scoped objects are maintained in pools. 
 * These pools are abstract in the sense they may be empty in reality.
 * If the pool does not contain the object it requests it to the passed resolver. 
 */
public interface ScopePool {

	
	public void clear();
	
	public <T> void add(WiringSpecification<T> spec , T object);
	
	public void remove(Object object);
	
	public <T> T getInScope (WiringSpecification<T> spec, Resolver<T> resolver);
	
	
	public void addScopeListener(ScopeListener listener);
	public void removeScopeListener(ScopeListener listener);
}
