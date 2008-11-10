package org.middleheaven.storage.datasource;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.middleheaven.core.Container;
import org.middleheaven.core.bootstrap.BootstrapService;
import org.middleheaven.core.services.AtivationException;
import org.middleheaven.core.services.Publish;
import org.middleheaven.core.services.Require;
import org.middleheaven.core.services.ServiceAtivatorContext;
import org.middleheaven.core.services.discover.ServiceActivator;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileFilter;
import org.middleheaven.logging.LogBook;
import org.middleheaven.logging.LoggingService;

/**
 * 
 * Searches for files with name end in -ds.properties and loads then.
 * This files have a structure like this:
 * (for jdbc)
 * datasource.name=dataSourceName 
 * datasource.url=jdbc:postgresql:database
 * datasource.driver=org.postgresql.Driver 
 * datasource.username=username
 * datasource.password=password
 * (for jndi)
 * datasource.url=jndi:java:/comp/env/jdbc/dsname
 */
public class DataSourceServiceActivator extends ServiceActivator {

	Map <String , DataSourceProvider> sources = new TreeMap <String , DataSourceProvider>();
	LogBook book;
	
	private BootstrapService bootstrapService;
	private LoggingService loggingService;
	private DataSourceService dataSourceService;
	
	@Publish
	public DataSourceService getDataSourceService() {
		return dataSourceService;
	}

	@Require
	public void setBootstrapService(BootstrapService bootstrapService) {
		this.bootstrapService = bootstrapService;
	}
	
	@Require
	public void setLoggingService(LoggingService loggingService) {
		this.loggingService = loggingService;
	}

	public void activate(ServiceAtivatorContext context){

		Container container =  bootstrapService.getContainer();

		book = loggingService.getLogBook(null);

		// look for the datasource mapping file

		ManagedFile folder = container.getAppConfigRepository();


		Collection<? extends ManagedFile> configurations = folder.listFiles(new ManagedFileFilter(){

			@Override
			public Boolean classify(ManagedFile file) {
				return file.getName().endsWith("-ds.properties");
			}


		});

		if (!configurations.isEmpty()){
			for (ManagedFile file : configurations){
				Properties connectionParams =new Properties();
				try {
					connectionParams.load(file.getContent().getInputStream());
					final String url = connectionParams.getProperty("datasource.url");
					final String protocol = url.substring(0,url.indexOf(':'));
					
					DataSourceProvider provider=null;
					
					if ("jdbc".equals(protocol)){
						provider = DriverManagerDSProvider.provider(connectionParams);
					} else if ("jndi".equals(protocol)){
						provider = JNDIDSProvider.provider(connectionParams);
					} else {
						book.error("Error loading datasource file. Provider type not recognized");
					}
					sources.put(connectionParams.getProperty("datasource.name"), provider );

					this.dataSourceService =  new HashDataSourceService();
				} catch (ManagedIOException e) {
					book.error("Error loading datasource file", e);
					throw new AtivationException(e);
				} catch (IOException e) {
					book.error("Error loading datasource file", e);
					throw new AtivationException(e);
				} 
			}

		}

		

	}
	


	private class HashDataSourceService implements DataSourceService{

		@Override
		public DataSource getDataSource(String name) {
			
			DataSourceProvider dsp = sources.get(name);
			if (dsp==null){
				throw new DataSourceProviderNotFoundException(name);
			}
			return dsp.getDataSource();
		}

		public void addDataSourceProvider(String name  , DataSourceProvider provider) {
			sources.put(name, provider);
		}
	}

	@Override
	public void inactivate(ServiceAtivatorContext context) {
		// no-op
	}
}
