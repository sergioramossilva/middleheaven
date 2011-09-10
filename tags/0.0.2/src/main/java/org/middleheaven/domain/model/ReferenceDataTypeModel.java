package org.middleheaven.domain.model;

import org.middleheaven.domain.model.DataTypeModel;

public interface ReferenceDataTypeModel extends DataTypeModel{

	public abstract String getTargetFieldName();

	public abstract Class<?> getTargetType();

	public abstract Class<?> getAggregationType();
	public abstract Class<?> getTargetFieldType();
}