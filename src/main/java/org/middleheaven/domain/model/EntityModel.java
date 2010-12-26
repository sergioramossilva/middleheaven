package org.middleheaven.domain.model;

import org.middleheaven.domain.model.EntityFieldModel;
import org.middleheaven.storage.QualifiedName;
import org.middleheaven.util.collections.Enumerable;

/**
 * The entity metadata holder.
 */
public interface EntityModel {

	/**
	 * 
	 * @return the name of the entity.
	 */
	public String getEntityName();
	
	/**
	 * 
	 * @return the entity class
	 */
	public Class<?> getEntityClass();
	
	/**
	 * 
	 * @param logicName the field's logic name
	 * @return the field entity model
	 */
	public EntityFieldModel fieldModel(QualifiedName logicName);
	
	/**
	 * 
	 * @return the identity field entity model
	 */
	public EntityFieldModel identityFieldModel();
	
	/**
	 * 
	 * @return the identity type
	 */
	public Class<?> getIdentityType();
	
	/**
	 * 
	 * @return all entity's field entity model.
	 */
	public Enumerable<? extends EntityFieldModel> fields();
}
