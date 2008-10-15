package org.middleheaven.storage.datasource;

import org.middleheaven.storage.StorageException;

public class DataSourceProviderNotFoundException extends StorageException {

	private String name;
	public DataSourceProviderNotFoundException(String name) {
		super("Data source provider was not found for " + name);
		this.name = name;
	}
	
	public String getDataSourceName() {
		return name;
	}

}
