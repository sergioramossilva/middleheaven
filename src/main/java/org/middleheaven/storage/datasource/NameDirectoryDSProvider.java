package org.middleheaven.storage.datasource;

import java.util.Properties;

import javax.sql.DataSource;

import org.middleheaven.core.wiring.annotations.Wire;
import org.middleheaven.core.wiring.service.Service;
import org.middleheaven.namedirectory.NameDirectoryService;
import org.middleheaven.namedirectory.NamingDirectoryException;
import org.middleheaven.storage.StorageException;

public class NameDirectoryDSProvider implements DataSourceProvider{

	NameDirectoryService service;
	
	@Wire
	public NameDirectoryDSProvider(@Service NameDirectoryService service){
		this.service = service;
	}
	
	public static NameDirectoryDSProvider provider(Properties properties){

		String  url = properties.getProperty("datasource.url");
		url = url.substring(url.indexOf(':')+1);
		return provider(url);
	}
	
	public static NameDirectoryDSProvider provider(String url){
		return new NameDirectoryDSProvider(url);
	}

	String url;
	public NameDirectoryDSProvider(String url){
		this.url = url;
	}
	
	DataSource ds;
	@Override
	public DataSource getDataSource() {
		if (ds==null){
			try {
				ds = service.lookup(url,DataSource.class);
			} catch (NamingDirectoryException e) {
				throw new StorageException(e);
			}
		}
		return ds;
	}
	

}
