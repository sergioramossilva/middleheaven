/**
 * 
 */
package org.middleheaven.core.wiring;

/**
 * 
 */
public class ObjectEvent {

	private Object object;

	/**
	 * Constructor.
	 * @param object
	 */
	public ObjectEvent(Object object) {
		this.object = object;
	}
	
	public Object getObject(){
		return object;
	}

}
