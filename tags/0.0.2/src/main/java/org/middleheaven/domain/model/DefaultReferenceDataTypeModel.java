package org.middleheaven.domain.model;

import org.middleheaven.domain.model.DataType;
import org.middleheaven.domain.model.ReferenceDataTypeModel;


public class DefaultReferenceDataTypeModel implements ReferenceDataTypeModel {

	private String fieldName;
	private DataType dataType;
	private Class<?> targetType;
	private Class<?> targetFieldType;
	private Class<?> aggregationType;
	
	protected DefaultReferenceDataTypeModel() {
		super();
	}
	
	public DefaultReferenceDataTypeModel(DataType dataType) {
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

	/* (non-Javadoc)
	 * @see org.middleheaven.domain.IReferenceDataTypeModel#getTargetFieldName()
	 */
	public String getTargetFieldName() {
		return fieldName;
	}
	
	
	/* (non-Javadoc)
	 * @see org.middleheaven.domain.IReferenceDataTypeModel#getTargetType()
	 */
	public Class<?> getTargetType() {
		return targetType;
	}

	public void setAggregationType(Class<?> aggregationType) {
		this.aggregationType = aggregationType;
	}

	/* (non-Javadoc)
	 * @see org.middleheaven.domain.IReferenceDataTypeModel#getAggregationType()
	 */
	public Class<?> getAggregationType() {
		return aggregationType;
	}

	@Override
	public Class<?> getTargetFieldType() {
		return targetFieldType;
	}
	
	public void setTargetFieldType( Class<?> type) {
		this.targetFieldType = type;
	}
	
}
