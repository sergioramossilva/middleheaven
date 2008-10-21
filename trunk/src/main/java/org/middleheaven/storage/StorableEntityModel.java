package org.middleheaven.storage;

import java.util.Collection;

import org.middleheaven.domain.EntityModel;


public interface StorableEntityModel extends EntityModel{

	public <E> Class<E> getEntityClass();
	public String getEntityHardName();
	public String logicNameForEntity();
	
	public StorableFieldModel fieldModel(QualifiedName logicName);
	public StorableFieldModel identityFieldModel();

	public Collection<StorableFieldModel> fields();

	public Object newInstance();
	Collection<StorableFieldModel> uniqueFields();
	

	
}
