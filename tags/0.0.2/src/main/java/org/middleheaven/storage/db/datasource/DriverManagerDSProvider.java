package org.middleheaven.storage.db.datasource;

import java.util.Properties;

import javax.sql.DataSource;

/**
 * Provides DataSource objects directly from the {@code DriverManager}
 */
public class DriverManagerDSProvider implements DataSourceProvider {

	public static DriverManagerDSProvider provider(Properties properties){

		String driver = properties.getProperty("datasource.driver");
		String  url = properties.getProperty("datasource.url");
		String username = properties.getProperty("datasource.username");
		String password = properties.getProperty("datasource.password");

		return provider( driver, url, username, password);

	}
	
	
	public static DriverManagerDSProvider provider(String driver,String url,String username,String password){

		return new DriverManagerDSProvider(new DriverDataSource( driver, url, username, password));

	}

	DriverDataSource ds;
	DriverManagerDSProvider(DriverDataSource ds){
		this.ds= ds;
		ds.setAutoCommit(true);
	}

	@Override
	public DataSource getDataSource() {
		return ds;
	}

}
