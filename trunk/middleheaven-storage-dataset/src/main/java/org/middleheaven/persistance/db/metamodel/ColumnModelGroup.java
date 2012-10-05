package org.middleheaven.persistance.db.metamodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ColumnModelGroup implements Iterable<DBColumnModel> {

	private List<DBColumnModel> columns = new ArrayList<DBColumnModel>();
	private String name;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addColumn(DBColumnModel column){
		columns.add(column);
	}
	
	public void removeColumn(DBColumnModel column){
		columns.remove(column);
	}
	
	
	@Override
	public Iterator<DBColumnModel> iterator() {
		return columns.iterator();
	}

	/**
	 * 
	 * @return
	 */
	public boolean isEmpty(){
		return columns.isEmpty();
	}
	
	/**
	 * 
	 * @return
	 */
	public int size(){
		return columns.size();
	}
}
