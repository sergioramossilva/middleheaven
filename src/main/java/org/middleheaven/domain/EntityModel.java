package org.middleheaven.domain;

import org.middleheaven.domain.store.QualifiedName;

/**
 * The entity metadata holder.
 */
public interface EntityModel extends Iterable<EntityFieldModel>{

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
	

}
