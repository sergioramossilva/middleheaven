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
 * 
 * datasource.name=nameOfDatasource 
 * datasource.url=jdbc:postgresql:database
 * datasource.driver=org.postgresql.Driver 
 * datasource.username=username
 * datasource.password=password
 * datasource.url=jdbc:postgresql:database
 */
public class DataSourceServiceActivator extends ServiceActivator {

	Map <String , DataSource> sources = new TreeMap <String , DataSource>();
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

					String driver = connectionParams.getProperty("datasource.driver");
					String  url = connectionParams.getProperty("datasource.url");
					String login = connectionParams.getProperty("datasource.username");
					String pass = connectionParams.getProperty("datasource.password");

					DriverDataSource dds =  new DriverDataSource(driver,url,login,pass);
					dds.setAutoCommit(true);
					
					sources.put(connectionParams.getProperty("datasource.name"), dds );


				} catch (ManagedIOException e) {
					book.logWarn("Error loading datasource file", e);
				} catch (IOException e) {
					book.logWarn("Error loading datasource file", e);
				} 
			}

		}

		context.register(DataSourceService.class, new HashDataSourceService(), null);

	}

	private <T> T lookup(String jndiName, Class<T> objectClass) throws NamingException {

		InitialContext context = new InitialContext();

		if (objectClass !=null){
			return objectClass.cast(PortableRemoteObject.narrow(context.lookup(jndiName), objectClass));
		} else {
			return objectClass.cast(context.lookup(jndiName));
		}
	}

	private class HashDataSourceService implements DataSourceService{

		@Override
		public DataSource getDataSource(String name) {
			// lookup locally
			DataSource ds = sources.get(name);
			if (ds==null){
				// lookup in JNDI
				try {
					ds = (DataSource) new InitialContext().lookup("java:/" + name);
				} catch (NamingException e) {
					book.logWarn("Fault looking for datasource " + name , e);
				}
			}
			return ds;
		}

		public void addDataSource(String name  , DataSource dataSource) {
			sources.put(name, dataSource);
		}
	}

	@Override
	public void inactivate(ServiceContext context) {
		// no-op
	}
}
