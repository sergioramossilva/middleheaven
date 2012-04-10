/**
 * 
 */
package org.middleheaven.core.wiring;

/**
 * 
 */
public class ScopePoolEvent {

	private Object object;
	private Scope scopePool;

	/**
	 * Constructor.
	 * @param object
	 */
	public ScopePoolEvent(Object object, Scope pool) {
		this.object = object;
		this.scopePool = pool;
	}

	/**
	 * Obtains {@link Object}.
	 * @return the object
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * Obtains {@link Scope}.
	 * @return the scopePool
	 */
	public Scope getScopePool() {
		return scopePool;
	}
	
	
	

}
