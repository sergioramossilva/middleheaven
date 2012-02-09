package org.middleheaven.persistance;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.middleheaven.util.QualifiedName;

/**
 * 
 */
public class HashDataRow implements DataRow {

	private final Map<QualifiedName , DataColumn> columns = new LinkedHashMap<QualifiedName, DataColumn>();

	public HashDataRow (){
		
	}
	
	public HashDataRow (DataRow other){
		for (DataColumn c : other){
			columns.put(c.getModel().getName(), c);
		}

	}
	
	public void addColumn(DataColumn column){
		columns.put(column.getModel().getName(), column);
	}
	
	@Override
	public Iterator<DataColumn> iterator() {
		return columns.values().iterator();
	}

	@Override
	public DataColumn getColumn(QualifiedName name) {
		return columns.get(name);
	}

}
