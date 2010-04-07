package org.middleheaven.domain;

import org.middleheaven.validation.Validator;

public class ReferenceDataTypeModel implements DataTypeModel {

	private String fieldName;
	private DataType dataType;
	private Class<?> targetType;
	private Class<?> aggregationType;
	
	protected ReferenceDataTypeModel() {
		super();
	}
	
	public ReferenceDataTypeModel(DataType dataType) {
		super();
		this.dataType = dataType;
	}

	@Override
	public DataType getDataType() {
		return dataType;
	}
	
	public void setTargetFieldName(String fieldName){
		this.fieldName = fieldName;
	}

	public void setTargetType(Class<?> targetType) {
		this.targetType =targetType; 
	}

	public String getTargetFieldName() {
		return fieldName;
	}

	public Class<?> getTargetType() {
		return targetType;
	}

	public void setAggregationType(Class<?> aggregationType) {
		this.aggregationType = aggregationType;
	}

	public Class<?> getAggregationType() {
		return aggregationType;
	}

	
}
