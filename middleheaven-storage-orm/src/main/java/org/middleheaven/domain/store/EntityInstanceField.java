package org.middleheaven.domain.store;

import org.middleheaven.domain.model.EntityFieldModel;

/**
 * An instance of an {@link EntityInstance} field. 
 */
public interface EntityInstanceField {

	/**
	 * 
	 * @return the field model.
	 */
	public EntityFieldModel getModel();
	
	/**
	 * 
	 * @return the field value.
	 */
	public Object getValue();
	
	/**
	 * 
	 * @param value the value to set.
	 */
	public void setValue(Object value);
	
	/**
	 * Add an item the the underling field Collection.
	 * Only works when the field is holding a Collection. 
	 * @param item the item to add.
	 */
	public void add(Object item);
	
	/**
	 * Removes an item the the underling field Collection.
	 * Only works when the field is holding a Collection. 
	 * @param item the item to remove.
	 */
	public void remove(Object item);
}
