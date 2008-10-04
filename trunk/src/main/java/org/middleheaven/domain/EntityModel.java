package org.middleheaven.domain;

import java.util.Collection;

import org.middleheaven.storage.QualifiedName;

public interface EntityModel {

	public Object newInstance();
	public String getEntityName();
	public Class<?> getEntityClass();
	
	public EntityFieldModel fieldModel(QualifiedName logicName);
	public EntityFieldModel identityFieldModel();

	public Collection<EntityFieldModel> fields();
}
