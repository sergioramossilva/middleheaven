package org.middleheaven.storage;

import java.util.Collection;


public interface StorableEntityModel{

	public Object newInstance();
	
	public String getEntityHardName();
	
	public StorableFieldModel fieldModel(QualifiedName logicName);
	public StorableFieldModel keyFieldModel();

	public Collection<StorableFieldModel> fields();
}
