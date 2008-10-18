package org.middleheaven.storage;

import java.util.Collection;


public interface StorableEntityModel{

	public String getEntityHardName();
	public String logicNameForEntity();
	
	public StorableFieldModel fieldModel(QualifiedName logicName);
	public StorableFieldModel keyFieldModel();

	public Collection<StorableFieldModel> fields();

	public <T> T newInstance();


	
}
