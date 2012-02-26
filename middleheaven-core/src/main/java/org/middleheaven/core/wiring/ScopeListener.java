/**
 * 
 */
package org.middleheaven.core.wiring;

/**
 * 
 */
public interface ScopeListener {

	/**
	 * @param scopePoolEvent
	 */
	void onObjectAdded(ScopePoolEvent scopePoolEvent);

	/**
	 * @param scopePoolEvent
	 */
	void onObjectRemoved(ScopePoolEvent scopePoolEvent);
	
	
}
