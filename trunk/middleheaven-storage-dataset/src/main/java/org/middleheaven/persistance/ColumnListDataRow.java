package org.middleheaven.persistance;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.middleheaven.util.QualifiedName;

/**
 * A {@link DataRow} constructed from a list of {@link DataColumn}s.
 */
public class ColumnListDataRow implements DataRow {

	
	private Map<QualifiedName, DataColumn> columns = new HashMap<QualifiedName, DataColumn>();
	
	/**
	 * 
	 * Constructor.
	 * @param model
	 */
	public ColumnListDataRow (){
	}
	
	/**
	 * Add a column to this row.
	 * @param column the column to add.
	 */
	public ColumnListDataRow add(DataColumn column){
		columns.put(column.getModel().getName(), column);
		return this;
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
