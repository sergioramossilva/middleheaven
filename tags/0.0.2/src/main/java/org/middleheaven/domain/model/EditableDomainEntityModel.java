package org.middleheaven.domain.model;

import org.middleheaven.util.collections.Enumerable;

public interface EditableDomainEntityModel extends EntityModel {

	/**
	 * 
	 * @param logicName the field's logic name
	 * @return the field entity model
	 */
	public EditableEntityFieldModel fieldModel(QualifiedName logicName);
	
	/**
	 * 
	 * @return the identity field entity model
	 */
	public EditableEntityFieldModel identityFieldModel();

	
	/**
	 * 
	 * @return all entity's field entity model.
	 */
	public Enumerable<? extends EditableEntityFieldModel> fields();

	/**
	 * 
	 * @param identityType
	 */
	public void setIdentityType(Class<?> identityType);

	
}
