package org.middleheaven.domain;

public interface ReferenceDataTypeModel extends DataTypeModel{

	public abstract String getTargetFieldName();

	public abstract Class<?> getTargetType();

	public abstract Class<?> getAggregationType();
	public abstract Class<?> getTargetFieldType();
}