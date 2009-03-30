package org.middleheaven.domain;


public interface FieldModelBuilder {

	FieldModelBuilder setTransient(boolean annotatedWith);

	FieldModelBuilder setIdentity(boolean b);

	FieldModelBuilder putParam(String string, String fieldName);

	FieldModelBuilder setDataType(DataType many_to_one);

	String getName();

	DataType getDataType();

	FieldModelBuilder setVersion(boolean annotatedWith);

	FieldModelBuilder setUnique(boolean annotatedWith);

	FieldModelBuilder setValueType(Class<?> valueType);

	boolean  isIdentity();

}
