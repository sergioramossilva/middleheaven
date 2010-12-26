package org.middleheaven.storage;

import java.util.Collection;

import org.middleheaven.model.domain.EntityModel;
import org.middleheaven.util.collections.Enumerable;

/**
 * Entity Model used during storage operations. 
 * This model enhances the original model with information relative to the target persistence media. 
 *
 */
public interface StorableEntityModel extends EntityModel {

	public String getEntityHardName();
	
	public String getEntityLogicName();
	
	public StorableFieldModel fieldModel(QualifiedName logicName);
	
	public StorableFieldModel identityFieldModel();

	Collection<StorableFieldModel> uniqueFields();
	
	public StorableFieldModel fieldReferenceTo(Class<?> type);

	/**
	 * 
	 * @return all entity's field entity model.
	 */
	public Enumerable<? extends StorableFieldModel> fields();
}
