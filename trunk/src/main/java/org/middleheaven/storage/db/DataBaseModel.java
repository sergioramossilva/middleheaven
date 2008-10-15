package org.middleheaven.storage.db;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class DataBaseModel implements Iterable<TableModel>{

	private Map<String,TableModel> tableModels = new TreeMap<String,TableModel>();

	public void addTable(TableModel tableModel) {
		tableModels.put(tableModel.getName(), tableModel);
	}

	@Override
	public Iterator<TableModel> iterator() {
		return tableModels.values().iterator();
	}

	public TableModel getTableModel(String name) {
		return tableModels.get(name);
	}
	
}
