package org.middleheaven.storage.db;

import org.middleheaven.domain.DataType;
import org.middleheaven.util.Hash;

public class ColumnModel implements Cloneable{

	private String name;
	private DataType type;
	private boolean indexed = false;
	private boolean unique = false;
	private boolean nullable = true;
	private boolean key = false;
	private int size;
	private int precision;
	TableModel tableModel;


	public ColumnModel(String name, DataType type) {
		super();
		this.name = name;
		this.type = type;
	}

	public TableModel getTableModel() {
		return tableModel;
	}

	public void setSize(int size){
		this.size =  size;
	}

	public boolean isIndexed() {
		return indexed;
	}

	public void setIndexed(boolean indexed) {
		this.indexed = indexed;
	}

	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DataType getType() {
		return type;
	}

	public void setType(DataType type) {
		this.type = type;
	}

	public int getSize() {
		return size;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public boolean isKey() {
		return key;
	}

	public void setKey(boolean key) {
		this.key = key;
	}

	public ColumnModel copy(TableModel tableModel) {
		ColumnModel nc = (ColumnModel) this.clone();
		nc.tableModel = tableModel;
		return nc;
	}

	public ColumnModel clone(){
		try {
			return (ColumnModel)super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
	
	public int hashCode(){
		return Hash.hash(this.name).hashCode();
	}

	public boolean equals(Object other){
		return other instanceof ColumnModel && ((ColumnModel)other).name.equals(name) && 
		this.tableModel.equals(((ColumnModel)other).tableModel);
	}
	
	public String toString(){
		return this.getTableModel().getName() + "." + this.name;
	}
}
