package org.middleheaven.domain.model;

import org.middleheaven.core.reflection.metaclass.MetaClass;

public interface ReferenceDataTypeModel extends DataTypeModel{

	public String getTargetFieldName();

	public MetaClass getTargetType();

	public MetaClass getAggregationType();
	public MetaClass getTargetFieldType();
	
}