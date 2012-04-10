/**
 * 
 */
package org.middleheaven.namedirectory.jndi;

import org.middleheaven.namedirectory.NameObjectEntry;

/**
 * 
 */
public class DetachedNameObjectEntry extends DetachedNameTypeEntry implements NameObjectEntry {

	private final Object object;

	/**
	 * Constructor.
	 * @param name
	 * @param typeName
	 */
	public DetachedNameObjectEntry(String name, String typeName, Object object) {
		super(name, typeName);
		this.object = object;
	}

	public Object getObject(){
		return this.object;
	}

}
