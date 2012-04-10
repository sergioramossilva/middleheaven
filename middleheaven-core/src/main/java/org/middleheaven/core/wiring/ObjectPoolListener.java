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
	void onObjectAdded(ObjectEvent objectPoolEvent);

	/**
	 * @param objectPoolEvent
	 */
	void onObjectRemoved(ObjectEvent objectPoolEvent);
}
