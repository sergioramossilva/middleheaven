/**
 * 
 */
package org.middleheaven.core.wiring;

/**
 * 
 */
public interface ObjectPoolListener {

	/**
	 * @param objectPoolEvent
	 */
	void onObjectAdded(ObjectPoolEvent objectPoolEvent);

	/**
	 * @param objectPoolEvent
	 */
	void onObjectRemoved(ObjectPoolEvent objectPoolEvent);
}
