package org.middleheaven.domain.model;

import org.middleheaven.domain.model.DataType;
import org.middleheaven.domain.model.DataTypeModel;
import org.middleheaven.storage.QualifiedName;

public interface EntityFieldModel {

	public QualifiedName getLogicName();
	public DataType getDataType();
	public boolean isTransient();
	public boolean isVersion();
	public boolean isIdentity();
	public Class<?> getValueType();
	public Class<?> getAggregationClass();
	public boolean isUnique();
	public boolean isNullable();

	public DataTypeModel getDataTypeModel();
	
}
