/**
 * 
 */
package org.middleheaven.process;

import org.middleheaven.util.coersion.TypeCoercing;

/**
 * 
 */
public class ObjectAttribute implements Attribute {

	private String name;
	private Object value;
	
	public ObjectAttribute (String name, Object value) {
		this.name = name;
		this.value = value;
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
	public Object getValue() {
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T getValue(Class<T> type) {
		return TypeCoercing.coerce(value, type);
	}

}
