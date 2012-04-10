/**
 * 
 */
package org.middleheaven.namedirectory.jndi;

import org.middleheaven.namedirectory.NameTypeEntry;
import org.middleheaven.util.Hash;

/**
 * Simple implementation for NameTypeEntry that mantains the data detached from the original source (commonly the JDNI API)
 */
class DetachedNameTypeEntry implements NameTypeEntry {


	private final String name;
	private final String typeName;

	/**
	 * 
	 * Constructor.
	 * @param name
	 * @param typeName
	 */
	public DetachedNameTypeEntry (String name, String typeName){
		this.name = name;
		this.typeName = typeName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTypeName() {
		return typeName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Hash.hash(name).hash(this.typeName).hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof DetachedNameTypeEntry) && equalsDetachedNameTypeEntry((DetachedNameTypeEntry)obj); 
	}


	private boolean equalsDetachedNameTypeEntry(DetachedNameTypeEntry other) {
		return this.name.equals(other.name) && this.typeName.equals(other.typeName);
	}


}
