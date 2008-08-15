package org.middleheaven.storage.criteria;

public interface FieldValueHolder {

	boolean isEmpty();
	public Object getValue();
	
	/**
	 * Compares the value holden by this with the value holden by other
	 * @param valueHolder
	 * @return
	 */
	boolean equalsValue(FieldValueHolder valueHolder);
}
