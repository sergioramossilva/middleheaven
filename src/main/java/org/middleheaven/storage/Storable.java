package org.middleheaven.storage;

import org.middleheaven.domain.EntityFieldModel;
import org.middleheaven.domain.EntityModel;
import org.middleheaven.util.identity.Identity;

public interface Storable {

	/**
	 * @return the type for the persistable object
	 */
	public Class<?> getPersistableClass();
	
	/**
	 * 
	 * @return the storable state.
	 */
	public StorableState getStorableState();
	
	/**
	 * 
	 * @param state the new state for this storable
	 */
	public void setStorableState(StorableState state);
	
	/**
	 * 
	 * @return the identity of this storable
	 */
	public Identity getIdentity();
	
	/**
	 * 
	 * @param id the identity for this storable
	 */
	public void setIdentity(Identity id);
	
	/**
	 * 
	 * @return the model for this storable
	 */
	public EntityModel getEntityModel();

	/**
	 * 
	 * @param model the field model
	 * @return the value associated with this field
	 */
	public Object getFieldValue(EntityFieldModel model);
	
	/**
	 * 
	 * @param model the field model.
	 * @param fieldValue the value to associate to this field
	 */
	public void setFieldValue(EntityFieldModel model, Object fieldValue);
	
	/**
	 * 
	 * @param model the field model
	 * @param element element to add in a ToMany relation with this storable
	 */
	public void addFieldElement(EntityFieldModel model, Object element);
	
	/**
	 * 
	 * @param model the field model
	 * @param element element to remove in a ToMany relation with this storable
	 */
	public void removeFieldElement(EntityFieldModel model, Object element);
}
