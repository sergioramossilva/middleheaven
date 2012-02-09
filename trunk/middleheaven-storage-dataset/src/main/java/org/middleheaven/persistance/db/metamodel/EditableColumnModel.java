package org.middleheaven.persistance.db.metamodel;

import org.middleheaven.persistance.model.ColumnType;
import org.middleheaven.util.Hash;
import org.middleheaven.util.QualifiedName;

public class EditableColumnModel implements DBColumnModel {

	private String name;
	private ColumnType type;
	
	private boolean version = false;
	private boolean indexed = false;
	private boolean unique = false;
	private boolean nullable = true;
	private boolean key = false;
	private int size = -1;
	private int precision = -1;
	
	DBTableModel tableModel;

	
	private String uniqueGroup;
	
	public EditableColumnModel(String name, ColumnType type) {
		super();
		this.name = name;
		this.type = type;
	}

	/**
	 * Copy Constructor.
	 * @param cm base {@link DBColumnModel} to copy.
	 */
	public EditableColumnModel(DBColumnModel cm) {
		this.uniqueGroup = cm.getUniqueGroupName();
		this.indexed = cm.isIndexed();
		this.key = cm.isKey();
		this.name = cm.getSimpleName();
		this.nullable = cm.isNullable();
		this.size = cm.getSize();
		this.precision = cm.getPrecision();
		this.type = cm.getType();
		this.tableModel = cm.getTableModel();
		this.version = cm.isVersion();
		
	}

	public DBTableModel getTableModel() {
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

	public String getSimpleName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ColumnType getType() {
		return type;
	}

	public void setType(ColumnType type) {
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
	
	@Override
	public DBColumnModel duplicate() {
		return new EditableColumnModel(this); 
	}
	
	public int hashCode(){
		return Hash.hash(this.name).hashCode();
	}

	public boolean equals(Object other){
		return other instanceof EditableColumnModel && ((EditableColumnModel)other).name.equalsIgnoreCase(name) && 
		this.tableModel.equals(((EditableColumnModel)other).tableModel);
	}
	
	public String toString(){
		return this.getTableModel().getName() + "." + this.name;
	}


	@Override
	public void setTableModel(DBTableModel tableModel) {
		this.tableModel = tableModel;
	}

	/**
	 * 
	 * @param version
	 */
	public void setVersion(boolean version) {
		this.version = version;
	}

	@Override
	public boolean isVersion() {
		return version;
	}

	public void setUniqueGroup(String group) {
		this.uniqueGroup = group;
	}


	@Override
	public String getUniqueGroupName() {
		return uniqueGroup;
	}

	@Override
	public QualifiedName getName() {
		return QualifiedName.qualify(this.tableModel.getName() , this.name);
	}

	public void setIndexGroup(String indexGroup) {
		// TODO Auto-generated method stub
		
	}
	
	


	
	
}
