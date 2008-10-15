package org.middleheaven.storage.db;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class TableModel implements Iterable<ColumnModel>{

	String name;
	List<ColumnModel> columns = new LinkedList<ColumnModel>();
	
	public TableModel(String name) {
		this.name = name;
	}
	public void addColumn(ColumnModel columnModel) {
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


}
