package org.middleheaven.storage.db;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DataBaseModel implements Iterable<DataBaseObjectModel>{

	private final Map<String,DataBaseObjectModel> tableModels = new HashMap<String,DataBaseObjectModel>();

	public void addDataBaseObjectModel(org.middleheaven.storage.db.DataBaseObjectModel dbtype) {
		tableModels.put(dbtype.getType().toString().toLowerCase() + ":" +dbtype.getName().toLowerCase(), dbtype);
	}
	
	public DataBaseObjectModel getDataBaseObjectModel(String name, DataBaseObjectType type) {
		return tableModels.get(type.toString().toLowerCase() + ":" +name.toLowerCase());
	}
	
	@Override
	public Iterator<DataBaseObjectModel> iterator() {
		return tableModels.values().iterator();
	}

	
	
}
