/**
 * 
 */
package org.middleheaven.core.wiring;

/**
 * 
 */
public interface ResolutionContext {

	/**
	 * @return
	 */
	String getScopeName();

	
	InstanceFactory getInstanceFactory(); 
}
