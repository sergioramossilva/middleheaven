package org.middleheaven.domain;


public interface FieldModelBuilder {

	FieldModelBuilder setTransient(boolean annotatedWith);

	FieldModelBuilder setIdentity(boolean b);
	
	FieldModelBuilder setDataType(DataType type);
	FieldModelBuilder setDataTypeModel(DataTypeModel typeModel);
	
	String getName();

	DataType getDataType();

	FieldModelBuilder setVersion(boolean annotatedWith);

	FieldModelBuilder setUnique(boolean annotatedWith);

	boolean isIdentity();


}
