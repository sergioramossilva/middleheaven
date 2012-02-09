package org.middleheaven.persistance.db.datasource;

import javax.sql.DataSource;


/**
 * A service that provides {@link DataSource} objects.
 */
public interface DataSourceService {

	public DataSource getDataSource(String name); 
	
	/**
	 * Register a {@link DataSourceProvider}. 
	 * 
	 * @param name the name o the dataSource, used from {@link #getDataSource(String)} to retrieve the {@link DataSource}.
	 * @param provider the provider.
	 */
	public void addDataSourceProvider(String name  , DataSourceProvider provider);
}
