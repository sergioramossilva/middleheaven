package org.middleheaven.domain.model;

import org.middleheaven.domain.model.DataType;
import org.middleheaven.domain.model.DataTypeModel;
import org.middleheaven.domain.model.FieldModelBuilder;


public interface FieldModelBuilder {


	FieldModelBuilder setIdentity(boolean b);
	
	FieldModelBuilder setDataType(DataType type);
	
	FieldModelBuilder setValueType(Class<?> valueType);
	
	FieldModelBuilder setDataTypeModel(DataTypeModel typeModel);
	
	String getName();

	DataType getDataType();

	FieldModelBuilder setVersion(boolean version);

	FieldModelBuilder setUnique(boolean unique);

	boolean isIdentity();

	FieldModelBuilder setNullable(boolean nullable);

	Class<?> getValueType();



}
