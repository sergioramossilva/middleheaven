package org.middleheaven.model;

import org.middleheaven.domain.model.DataType;
import org.middleheaven.domain.model.DataTypeModel;
import org.middleheaven.domain.model.QualifiedName;

public interface FieldModel {

	/**
	 * This is the identifier field;
	 * @return <code>true</code> if this is the identifier field;
	 */
	public boolean isIdentity();
	
	
	/**
	 * This is a version field. 
	 * @return <code>true</code> is this field is the version container.
	 */
	public boolean isVersion();
	
	/**
	 * The field's logic name.
	 * @return the qualified name. The qualifier is the entity/class name. 
	 */
	public QualifiedName getName();
	
	/**
	 * 
	 * @return <code>true</code> if this field is unique.
	 */
	public boolean isUnique();
	
	/**
	 * 
	 * @return the name of the unique group, ou <code>null</code> if does not apply.
	 */
	public String getUniqueGroup();
	
	/**
	 * 
	 * @return <code>true</code> if this field is nullable, <code>false</code> is this field is always mandatory.
	 */
	public boolean isNullable();


	public Class<?> getValueType();
	public Class<?> getAggregationClass();

	public DataType getDataType();
	public DataTypeModel getDataTypeModel();
	
}
