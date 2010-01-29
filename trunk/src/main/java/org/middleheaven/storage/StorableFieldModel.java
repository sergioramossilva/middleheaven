package org.middleheaven.storage;

import org.middleheaven.domain.EntityFieldModel;

public interface StorableFieldModel extends EntityFieldModel{


	public QualifiedName getHardName();
	public StorableDataTypeModel getDataTypeModel();
	
	StorableEntityModel getEntityModel();
	

}
