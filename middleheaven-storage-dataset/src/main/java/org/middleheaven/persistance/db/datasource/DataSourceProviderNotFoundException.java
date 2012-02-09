package org.middleheaven.persistance.db.datasource;

import org.middleheaven.persistance.PersistanceException;

public class DataSourceProviderNotFoundException extends PersistanceException {

	private String name;
	public DataSourceProviderNotFoundException(String name) {
		super("Data source provider was not found for " + name);
		this.name = name;
	}
	
	public String getDataSourceName() {
		return name;
	}

}
