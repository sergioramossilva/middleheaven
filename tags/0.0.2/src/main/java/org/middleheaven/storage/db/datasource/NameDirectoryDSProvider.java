package org.middleheaven.storage.db.datasource;

import java.util.Properties;

import javax.sql.DataSource;

import org.middleheaven.namedirectory.NameDirectoryService;
import org.middleheaven.namedirectory.NamingDirectoryException;
import org.middleheaven.storage.StorageException;

/**
 * Provides DataSource objects from a location in a NameDirectoryService
 */
public class NameDirectoryDSProvider implements DataSourceProvider{

	private NameDirectoryService service;
	private String url;

	public static NameDirectoryDSProvider provider(NameDirectoryService service, String url){
		return new NameDirectoryDSProvider(service,url);
	}
	
	public static NameDirectoryDSProvider provider(NameDirectoryService service, Properties properties){
		String  url = properties.getProperty("datasource.url");
		url = url.substring(url.indexOf(':')+1);

		return provider(service,url);
	}

	private NameDirectoryDSProvider(NameDirectoryService service,String url){
		this.service = service;
		this.url = url;
	}


	@Override
	public DataSource getDataSource() {
		try {
			return service.lookup(url,DataSource.class);
		} catch (NamingDirectoryException e) {
			throw new StorageException(e);
		}
	}


}
