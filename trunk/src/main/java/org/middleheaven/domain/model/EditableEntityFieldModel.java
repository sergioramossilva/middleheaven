package org.middleheaven.domain.model;


public interface EditableEntityFieldModel extends EntityFieldModel{


	public void setDataType(DataType dataType);

	public void setIsIdentity(boolean isIdentity);

	public void setValueType(Class<?> valueType);

	public void setVersion(boolean isVersion);

	public void setUnique(boolean isUnique);

	public Class<?> getAggregationClass();
	
	public void setAggregationClass(Class<?> aggregationClass);

	public void setDataTypeModel(DataTypeModel dataTypeModel);

	public void setNullable(boolean isNullable);

	public void setIdentity(boolean isIdentity);

	
	

}
