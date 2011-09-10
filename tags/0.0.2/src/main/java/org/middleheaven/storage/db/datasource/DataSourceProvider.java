package org.middleheaven.storage.db.datasource;

import javax.sql.DataSource;

public interface DataSourceProvider {

	
	public DataSource getDataSource();
}
