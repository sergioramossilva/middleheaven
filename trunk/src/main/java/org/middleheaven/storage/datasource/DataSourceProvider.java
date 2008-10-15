package org.middleheaven.storage.datasource;

import javax.sql.DataSource;

public interface DataSourceProvider {

	
	public DataSource getDataSource();
}
