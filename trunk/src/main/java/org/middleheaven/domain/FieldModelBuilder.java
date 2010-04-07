package org.middleheaven.domain;


public interface FieldModelBuilder {

	FieldModelBuilder setTransient(boolean annotatedWith);

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


}
