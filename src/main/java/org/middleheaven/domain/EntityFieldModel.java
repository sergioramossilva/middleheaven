package org.middleheaven.domain;

import org.middleheaven.domain.store.QualifiedName;

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
