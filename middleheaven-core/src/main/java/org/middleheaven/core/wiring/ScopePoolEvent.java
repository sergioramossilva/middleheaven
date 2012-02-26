/**
 * 
 */
package org.middleheaven.core.wiring;

/**
 * 
 */
public class ScopePoolEvent {

	private Object object;
	private ScopePool scopePool;

	/**
	 * Constructor.
	 * @param object
	 */
	public ScopePoolEvent(Object object, ScopePool pool) {
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
	 * Obtains {@link ScopePool}.
	 * @return the scopePool
	 */
	public ScopePool getScopePool() {
		return scopePool;
	}
	
	
	

}
