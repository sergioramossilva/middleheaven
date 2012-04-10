/**
 * 
 */
package org.middleheaven.namedirectory;

/**
 * 
 */
public interface NameObjectEntry extends NameTypeEntry{

	/**
	 * 
	 * @return the object associated with the name. This object if of the class represented by <code>getTypeName</code>
	 */
	public Object getObject();
}
