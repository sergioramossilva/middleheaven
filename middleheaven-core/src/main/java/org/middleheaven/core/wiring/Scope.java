package org.middleheaven.core.wiring;

/**
 * Interface for all pools that belong to a scope.
 * scoped objects are maintained in pools. 
 * These pools are abstract in the sense they may be empty in reality.
 * If the pool does not contain the object it requests it to the passed resolver. 
 */
public interface Scope {

	/**
	 * Remove all objects from scope.
	 */
	public void clear();
		
	/**
	 * Remove the specified object from the scope.
	 * 
	 * @param object
	 */
	public void remove(Object object);
	
	
	/**
	 * Obtain the specified object from this scope. If the object is not in the scope,
	 * use the resolver to retrieve it , and then put in the scope.
	 * @param context the resolution context
	 * @param query the procured object specification
	 * @param resolver the resolver to create the object if it is not in scope.
	 * 
	 * @return
	 */
	public Object getInScope (ResolutionContext context, WiringQuery query, Resolver resolver);
	
	/**
	 * Add a scope listener
	 * 
	 * @param listener a listener to add.
	 */
	public void addScopeListener(ScopeListener listener);
	
	/**
	 * remove a scope listener.
	 * 
	 * @param listener the listener to remove
	 */
	public void removeScopeListener(ScopeListener listener);
}
