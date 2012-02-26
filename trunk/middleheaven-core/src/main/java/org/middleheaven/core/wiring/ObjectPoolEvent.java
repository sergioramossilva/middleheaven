/**
 * 
 */
package org.middleheaven.core.wiring;

/**
 * 
 */
public class ObjectPoolEvent {

	private Object object;

	/**
	 * Constructor.
	 * @param object
	 */
	public ObjectPoolEvent(Object object) {
		this.object = object;
	}
	
	public Object getObject(){
		return object;
	}

}
