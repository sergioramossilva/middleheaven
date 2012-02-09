package org.middleheaven.domain.model;

import org.middleheaven.core.reflection.metaclass.MetaClass;

public interface ReferenceDataTypeModel extends DataTypeModel{

	public abstract String getTargetFieldName();

	public abstract MetaClass getTargetType();

	public abstract MetaClass getAggregationType();
	public abstract MetaClass getTargetFieldType();
}