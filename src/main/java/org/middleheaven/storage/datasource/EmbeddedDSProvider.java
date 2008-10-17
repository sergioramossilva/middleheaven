package org.middleheaven.storage.datasource;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javax.sql.DataSource;

public class EmbeddedDSProvider implements DataSourceProvider{


	public static EmbeddedDSProvider provider(Properties properties){

		try {
			String  url = properties.getProperty("datasource.url");
			String username = properties.getProperty("datasource.username");
			String password = properties.getProperty("datasource.password");
			String catalog = properties.getProperty("datasource.catalog");
			return provider(new URL(url),catalog, username, password);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} 

	}

	public static EmbeddedDSProvider provider(URL url,String catalog, String username,String password){
		return new EmbeddedDSProvider(new EmbeddedDataSource(url,catalog, username, password));
	}


	EmbeddedDataSource ds;
	private EmbeddedDSProvider(EmbeddedDataSource ds) {
		this.ds = ds;
		ds.setAutoCommit(true);
	}

	@Override
	public DataSource getDataSource() {
		return ds;
	}

	public void start(){
		ds.start();
	}

	public void stop(){
		ds.stop();
	}
}
