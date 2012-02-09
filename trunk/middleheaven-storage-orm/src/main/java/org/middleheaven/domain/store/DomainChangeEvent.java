/**
 * 
 */
package org.middleheaven.domain.store;

/**
 * 
 */
public class DomainChangeEvent {

	private Object instance;

	/**
	 * Constructor.
	 * @param instance
	 */
	public DomainChangeEvent(Object instance) {
		this.instance = instance;
	}

	/**
	 * @return
	 */
	public Object getInstance() {
		return instance;
	}
}
