/**
 * 
 */
package org.middleheaven.core.wiring;

import java.lang.reflect.Field;

/**
 * 
 */
class FieldWiringTarget implements WiringTarget {

	private Field field;
	private Object instance;

	/**
	 * Constructor.
	 * @param field
	 * @param object
	 */
	public FieldWiringTarget(Field field, Object instance) {
		this.field = field;
		this.instance = instance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getType() {
		return field.getType();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getDeclaringType() {
		return field.getDeclaringClass();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getInstance() {
		return instance;
	}

}
