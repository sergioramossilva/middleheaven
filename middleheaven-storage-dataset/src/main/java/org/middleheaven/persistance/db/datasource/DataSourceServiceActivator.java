package org.middleheaven.persistance.db.datasource;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.middleheaven.core.bootstrap.BootstrapContainer;
import org.middleheaven.core.bootstrap.BootstrapService;
import org.middleheaven.core.bootstrap.activation.ServiceActivator;
import org.middleheaven.core.bootstrap.activation.ServiceSpecification;
import org.middleheaven.core.services.AtivationException;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.logging.Log;
import org.middleheaven.namedirectory.NameDirectoryService;
import org.middleheaven.namedirectory.NameObjectEntry;
import org.middleheaven.util.classification.BooleanClassifier;

/**
 * If a {@link NameDirectoryService} import all {@link DataSource} present at <code>java:/comp/env/jdbc/</code>.
 * Otherwise, earches for files with name end in -ds.properties and loads then.
 * This files have a structure like this:
 * (for jdbc)
 * datasource.name=dataSourceName 
 * datasource.url=jdbc:postgresql:database
 * datasource.driver=org.postgresql.Driver 
 * datasource.username=username
 * datasource.password=password
 * (for jndi)
 * datasource.name=dataSourceName
 * datasource.url=jndi:java:/comp/env/jdbc/dsname
 */
public class DataSourceServiceActivator extends ServiceActivator {

	Map <String , DataSourceProvider> sources = new HashMap <String , DataSourceProvider>();

	private HashDataSourceService dataSourceService;

	private static final String JNDI_DATASOURCE_DIRECTORY = "java:/comp/env/jdbc/";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectRequiredServicesSpecifications(Collection<ServiceSpecification> specs) {
		specs.add(new ServiceSpecification(NameDirectoryService.class, true));
		specs.add(new ServiceSpecification(BootstrapContainer.class));

	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectPublishedServicesSpecifications(Collection<ServiceSpecification> specs) {
		specs.add(new ServiceSpecification(DataSourceService.class));
	}
	
	@Override
	public void activate(ServiceContext serviceContext ) {



		NameDirectoryService nameDirectoryService = serviceContext.getService(NameDirectoryService.class);


		if (nameDirectoryService != null){


			for (NameObjectEntry entry : nameDirectoryService.listObjects(JNDI_DATASOURCE_DIRECTORY)){

				if (entry.getObject() instanceof DataSource) {
					NameDirectoryLookupDSProvider provider = NameDirectoryLookupDSProvider.provider(nameDirectoryService , JNDI_DATASOURCE_DIRECTORY + entry.getName());

					sources.put(entry.getName(), provider );
				}

			}

		} else {
			// look for the datasource mapping file
			BootstrapContainer container =  serviceContext.getService(BootstrapService.class).getEnvironmentBootstrap().getContainer();

			ManagedFile folder = container.getFileSystem().getAppConfigRepository();

			Collection<? extends ManagedFile> configurations = folder.children().findAll(new BooleanClassifier<ManagedFile>(){

				@Override
				public Boolean classify(ManagedFile file) {
					return file.getPath().getFileName().endsWith("-ds.properties");
				}


			});


			if (!configurations.isEmpty()){
				for (ManagedFile file : configurations){
					Properties connectionParams = new Properties();
					try {
						connectionParams.load(file.getContent().getInputStream());
						final String url = connectionParams.getProperty("datasource.url");
						final String protocol = url.substring(0,url.indexOf(':'));

						DataSourceProvider provider=null;

						if ("jdbc".equals(protocol)){
							provider = DriverManagerDSProvider.provider(connectionParams);
						} else {

							if ("jndi".equals(protocol)){
								if (nameDirectoryService == null){
									Log.onBookFor(this.getClass()).warn("DataSource configuration uses a Name and Directory location, but a Name Directory Service was not found");
									continue;
								}
								provider = NameDirectoryLookupDSProvider.provider(nameDirectoryService , connectionParams);
							} else {
								Log.onBookFor(this.getClass()).error("Error loading datasource file. Provider type not recognized");
							}
						}
						sources.put(connectionParams.getProperty("datasource.name"), provider );


					} catch (ManagedIOException e) {
						Log.onBookFor(this.getClass()).error(e,"Error loading datasource file");
						throw new AtivationException(e);
					} catch (IOException e) {
						Log.onBookFor(this.getClass()).error(e,"Error loading datasource file");
						throw new AtivationException(e);
					} 
				}

			}

		}


		this.dataSourceService =  new HashDataSourceService();

		serviceContext.register(DataSourceService.class, dataSourceService);

	}



	private class HashDataSourceService implements DataSourceService{


		public HashDataSourceService(){}

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

		/**
		 * 
		 */
		public void close(){

		}
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inactivate(ServiceContext serviceContext) {
		this.dataSourceService.close();

		serviceContext.unRegister(DataSourceService.class);
	}



}
