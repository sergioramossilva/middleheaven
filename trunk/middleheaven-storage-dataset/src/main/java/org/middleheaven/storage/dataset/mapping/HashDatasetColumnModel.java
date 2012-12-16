/**
 * 
 */
package org.middleheaven.storage.dataset.mapping;

import org.middleheaven.persistance.model.ColumnValueType;

/**
 * 
 */
public class HashDatasetColumnModel implements EditableDatasetColumnModel{
	
	private String name;
	private String hardname;
	private int length = 0;
	private int scale = 0;
	private int precision = 0;
	private boolean updatable = true;
	private boolean insertable = true;
	private boolean nullable = true;
	private boolean unique = false;
	private String uniqueGroupName = "";
	private ColumnValueType valueType;
	private boolean key = false;
	private boolean indexed;
	private boolean version;
	
	/**
	 * Constructor.
	 * @param column
	 */
	public HashDatasetColumnModel(DatasetColumnModel column) {
		this.name = column.getName();
		this.hardname = column.getHardName();
		this.length = column.getSize();
		this.scale = column.getScale();
		this.precision = column.getPrecision();
		this.indexed = column.isIndexed();
		this.insertable = column.isInsertable();
		this.key = column.isKey();
		this.nullable = column.isNullable();
		this.unique = column.isUnique();
		this.uniqueGroupName = column.getUniqueGroupName();
		this.updatable = column.isUpdatable();
		this.valueType = column.getValueType();
		// this.version = column.i // TODO
	}

	
	public HashDatasetColumnModel() {
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getHardName() {
		return hardname;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSize() {
		return length;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getScale() {
		return scale;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getPrecision() {
		return precision;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isUpdatable() {
		return updatable;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInsertable() {
		return insertable;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNullable() {
		return nullable;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isUnique() {
		return this.unique;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUniqueGroupName() {
		return this.uniqueGroupName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHardName(String hardname) {
		this.hardname = hardname;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSize(int length) {
		this.length = length;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setScale(int scale) {
		 this.scale = scale;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPrecision(int precision) {
		this.precision = precision;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUpdatable(boolean updatable) {
		this.updatable = updatable; 
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInsertable(boolean insertable) {
		this.insertable = insertable;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUniqueGroupName(String uniqueGroupName) {
		this.uniqueGroupName = uniqueGroupName;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public ColumnValueType getValueType() {
		return this.valueType;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isKey() {
		return key;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValueType(ColumnValueType type) {
		this.valueType = type;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVersion(boolean version) {
		this.version = version;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setKey(boolean key) {
		this.key = key;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setIndexed(boolean indexed) {
		this.indexed = indexed;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isIndexed() {
		return this.indexed;
	}

}
