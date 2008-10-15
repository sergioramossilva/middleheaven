package org.middleheaven.storage.datasource;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;

import org.middleheaven.core.Container;
import org.middleheaven.core.services.ContainerService;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.engine.ServiceActivator;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileFilter;
import org.middleheaven.logging.LogBook;
import org.middleheaven.logging.LoggingService;

/**
 * 
 * Searches for files with name end in -ds.properties and loads then.
 * This files have a structure like this:
 * datasource.provider = jdbc | jndi
 * datasource.name=nameOfDatasource 
 * datasource.url=jdbc:postgresql:database
 * datasource.driver=org.postgresql.Driver 
 * datasource.username=username
 * datasource.password=password
 */
public class DataSourceServiceActivator extends ServiceActivator {

	Map <String , DataSourceProvider> sources = new TreeMap <String , DataSourceProvider>();
	LogBook book;
	public void activate(ServiceContext context){

		Container container =  context.getService(ContainerService.class, null).getContainer();
		book = context.getService(LoggingService.class, null).getLogBook("middleheaven");

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
					String providerType = connectionParams.getProperty("datasource.provider");
					DataSourceProvider provider=null;
					
					if ("jdbc".equals(providerType.trim())){
						provider = DriverManagerDSProvider.provider(connectionParams);
					} else if ("jndi".equals(providerType.trim())){
						provider = JNDIDSProvider.provider(connectionParams);
					} else {
						book.logError("Error loading datasource file. Provider type not recognized");
					}
					sources.put(connectionParams.getProperty("datasource.name"), provider );


				} catch (ManagedIOException e) {
					book.logError("Error loading datasource file", e);
				} catch (IOException e) {
					book.logError("Error loading datasource file", e);
				} 
			}

		}

		context.register(DataSourceService.class, new HashDataSourceService(), null);

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
	public void inactivate(ServiceContext context) {
		// no-op
	}
}
