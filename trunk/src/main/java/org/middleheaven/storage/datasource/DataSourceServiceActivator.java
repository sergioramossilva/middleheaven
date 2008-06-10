package org.middleheaven.storage.datasource;

import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.engine.ServiceActivator;

public class DataSourceServiceActivator extends ServiceActivator {

	Map <String , DataSource> sources = new TreeMap <String , DataSource>();
	public void activate(ServiceContext context){
		
		
		// look for the datasource mapping file
		
		// read data form it
		
		String url;
		// handle url
		
		if (url.startsWith("jndi:")){
			
		} else if (url.startsWith("jdbc:")){
			
		}
		
		context.register(DataSourceService.class, new HashDataSourceService(), null);
		
	}
	
	private class HashDataSourceService implements DataSourceService{

		@Override
		public DataSource getDataSource(String name) {
			return sources.get(name);
		}
	}

	@Override
	public void inactivate(ServiceContext context) {
		// TODO Auto-generated method stub
		
	}
}
