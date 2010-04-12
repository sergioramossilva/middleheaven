package org.middleheaven.domain;

import org.middleheaven.storage.QualifiedName;

public class EditableEntityFieldModel implements EntityFieldModel{

	QualifiedName name;
	private DataType dataType;
	private boolean isIdentity;
	private Class<?> valueType;
	private boolean isTransient;
	private boolean isVersion;
	private boolean isUnique;
	private boolean isNullable;
	
	private Class<?> aggregationClass;
	private DataTypeModel dataTypeModel = new AbstractDataTypeModel(){

		@Override
		public DataType getDataType() {
			return dataType;
		}
		
	};
	


	public EditableEntityFieldModel(String entityName, String name) {
		this.name = QualifiedName.qualify(entityName, name);
	}
	
	@Override
	public QualifiedName getLogicName() {
		return name;
	}
	
	@Override
	public DataType getDataType() {
		return this.dataType;
	}

	@Override
	public Class<?> getValueType() {
		return valueType;
	}

	@Override
	public boolean isIdentity() {
		return isIdentity;
	}

	@Override
	public boolean isTransient() {
		return isTransient;
	}

	@Override
	public boolean isUnique() {
		return this.isUnique;
	}

	@Override
	public boolean isVersion() {
		return isVersion;
	}



	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public void setIsIdentity(boolean isIdentity) {
		this.isIdentity = isIdentity;
	}

	public void setTransient(boolean isTransient) {
		this.isTransient = isTransient;
	}

	public void setValueType(Class<?> valueType) {
		this.valueType = valueType;
	}

	public void setVersion(boolean isVersion) {
		this.isVersion = isVersion;
	}

	public void setUnique(boolean isUnique) {
		this.isUnique = isUnique;
	}


	@Override
	public Class<?> getAggregationClass() {
		return aggregationClass;
	}
	
	public void setAggregationClass(Class<?> aggregationClass) {
		this.aggregationClass = aggregationClass;
	}

	@Override
	public String toString() {
		return "EditableEntityFieldModel [aggregationClass=" + aggregationClass
				+ ", dataType=" + dataType + ", isIdentity=" + isIdentity
				+ ", isTransient=" + isTransient + ", isUnique=" + isUnique + ", isNullable=" + isNullable
				+ ", isVersion=" + isVersion + ", name=" + name + ", valueType=" + valueType + "]";
	}

	@Override
	public DataTypeModel getDataTypeModel() {
		return dataTypeModel;
	}

	public void setDataTypeModel(DataTypeModel dataTypeModel) {
		this.dataTypeModel = dataTypeModel;
	}

	public void setNullable(boolean isNullable) {
		this.isNullable = isNullable;
	}
	
	@Override
	public boolean isNullable() {
		return isNullable;
	}
	
	

}
