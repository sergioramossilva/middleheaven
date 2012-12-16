package org.middleheaven.domain.model;

import org.middleheaven.util.QualifiedName;

public interface FieldModel {

	/**
	 * This is the discriminator field. Discriminator fields holded value allows to diferenciate 
	 * between diferente classes in an single table inherintance strategy
	 * @return <code>true</code> if this is a discriminator field;
	 */
	public boolean isDiscriminator();
	
	/**
	 * This is a transient field. Transiente fields are not mapped to persistenace.
	 * @return <code>true</code> if this is a transient field;
	 */
	public boolean isTransient();
	
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
