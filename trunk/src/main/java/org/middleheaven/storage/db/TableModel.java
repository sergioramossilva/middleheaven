package org.middleheaven.storage.db;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

import org.middleheaven.util.Hash;


public class TableModel implements Iterable<ColumnModel>, DataBaseObjectModel{

	String name;
	Collection<ColumnModel> columns = new LinkedHashSet<ColumnModel>();
	
	public TableModel(String name) {
		this.name = name;
	}
	public void addColumn(ColumnModel columnModel) {
		if (columnModel.getName() == null){
			throw new IllegalArgumentException("Column name cannot be null");
		}
		if( columnModel.getTableModel()== null){
			columnModel.model = this;
			columns.add(columnModel);
		}
		
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public Iterator<ColumnModel> iterator() {
		return columns.iterator();
	}
	@Override
	public DataBaseObjectType getType() {
		return DataBaseObjectType.TABLE;
	}

	public TableModel differenceTo(TableModel other) {
		// Create a table model with the columns that existe in ther this model that not exist on the other 
		TableModel res = new TableModel(this.name);
		
		for (ColumnModel c : this.columns){
			if (!other.columns.contains(c)){
				res.addColumn(c.copy(this));
			}
		}
		
		return res;
	}
	
	public boolean equals(Object other){
		return other instanceof TableModel && ((TableModel)other).name.equals(this.name);
	}
	
	public int hashCode(){
		return Hash.hash(this.name).hashCode();
	}

}
