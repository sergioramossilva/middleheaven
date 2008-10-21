package org.middleheaven.storage;

import org.middleheaven.data.DataType;
import org.middleheaven.domain.EntityFieldModel;

public interface StorableFieldModel extends EntityFieldModel{

	
	public QualifiedName getHardName();
	public QualifiedName getLogicName();
	public String getParam(String string);
	public DataType getDataType();
	public boolean isTransient();
	public boolean isVersion();
	public boolean isKey();
	public Class<?> getValueClass();
	public boolean isUnique();
	
	StorableEntityModel getEntityModel();

}
