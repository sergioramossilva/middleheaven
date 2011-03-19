package org.middleheaven.domain.model;

import org.middleheaven.domain.model.DataType;
import org.middleheaven.domain.model.DataTypeModel;
import org.middleheaven.domain.model.EntityFieldModel;
import org.middleheaven.domain.model.SimpleDataTypeModel;

public class BeanEditableEntityFieldModel implements EditableEntityFieldModel {

	QualifiedName name;
	private DataType dataType;
	private boolean isIdentity;
	private Class<?> valueType;
	private boolean isVersion;
	private boolean isUnique;
	private boolean isNullable;
	
	private Class<?> aggregationClass;
	private DataTypeModel dataTypeModel;
	private String uniqueGroup;

	public BeanEditableEntityFieldModel(String entityName, String name) {
		this.name = QualifiedName.qualify(entityName, name);
		dataTypeModel = new SimpleDataTypeModel(this);
	}
	
	@Override
	public QualifiedName getName() {
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
	public boolean isUnique() {
		return this.isUnique;
	}

	@Override
	public boolean isVersion() {
		return isVersion;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setIsIdentity(boolean isIdentity) {
		this.isIdentity = isIdentity;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValueType(Class<?> valueType) {
		this.valueType = valueType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVersion(boolean isVersion) {
		this.isVersion = isVersion;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUnique(boolean isUnique) {
		this.isUnique = isUnique;
	}


	@Override
	public Class<?> getAggregationClass() {
		return aggregationClass;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAggregationClass(Class<?> aggregationClass) {
		this.aggregationClass = aggregationClass;
	}

	@Override
	public String toString() {
		return "EditableEntityFieldModel [aggregationClass=" + aggregationClass
				+ ", dataType=" + dataType + ", isIdentity=" + isIdentity
				+ ", isUnique=" + isUnique + ", isNullable=" + isNullable
				+ ", isVersion=" + isVersion + ", name=" + name + ", valueType=" + valueType + "]";
	}

	@Override
	public DataTypeModel getDataTypeModel() {
		return dataTypeModel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDataTypeModel(DataTypeModel dataTypeModel) {
		this.dataTypeModel = dataTypeModel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNullable(boolean isNullable) {
		this.isNullable = isNullable;
	}
	
	@Override
	public boolean isNullable() {
		return isNullable;
	}

	@Override
	public void setIdentity(boolean isIdentity) {
		this.isIdentity = isIdentity;
	}

	@Override
	public String getUniqueGroup() {
		return uniqueGroup;
	}
	
	

}
