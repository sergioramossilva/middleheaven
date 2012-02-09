package org.middleheaven.domain.model;

import org.middleheaven.core.reflection.metaclass.MetaClass;
import org.middleheaven.util.QualifiedName;
import org.middleheaven.util.collections.Enumerable;

/**
 * The entity metadata holder.
 */
public interface EntityModel  {

	
	/**
	 * 
	 * @return the name of the entity.
	 */
	public String getEntityName();
	
	/**
	 * 
	 * @return the entity class
	 */
	public MetaClass getEntityClass();
	
	/**
	 * 
	 * @param logicName the field's logic name
	 * @return the field entity model
	 */
	public EntityFieldModel fieldModel(QualifiedName logicName);
	
	/**
	 * @param fieldQualifiedName
	 * @return
	 */
	public boolean containsField(QualifiedName fieldQualifiedName);
	
	/**
	 * 
	 * @return the identity field entity model
	 */
	public EntityFieldModel identityFieldModel();
	
	/**
	 * 
	 * @return the identity type
	 */
	public MetaClass getIdentityType();
	
	/**
	 * 
	 * @return all entity's field entity model.
	 */
	public Enumerable<? extends EntityFieldModel> fields();

	/**
	 * @return
	 */
	public boolean isIdentityAssigned();
}
