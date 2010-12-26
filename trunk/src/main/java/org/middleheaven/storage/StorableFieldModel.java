package org.middleheaven.storage;

import org.middleheaven.model.domain.EntityFieldModel;

public interface StorableFieldModel extends EntityFieldModel{


	public QualifiedName getHardName();
	public StorableDataTypeModel getDataTypeModel();
	
	StorableEntityModel getEntityModel();
	

}
