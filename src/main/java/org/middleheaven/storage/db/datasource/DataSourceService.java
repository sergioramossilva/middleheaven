package org.middleheaven.storage.db.datasource;

import javax.sql.DataSource;

public interface DataSourceService {

	public DataSource getDataSource(String name); 
	public void addDataSourceProvider(String name  , DataSourceProvider provider);
}
