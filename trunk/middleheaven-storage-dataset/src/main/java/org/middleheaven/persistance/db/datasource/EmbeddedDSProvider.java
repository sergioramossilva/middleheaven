package org.middleheaven.persistance.db.datasource;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javax.sql.DataSource;

import org.middleheaven.core.bootstrap.BootstapListener;
import org.middleheaven.core.bootstrap.BootstrapEvent;
import org.middleheaven.core.bootstrap.BootstrapService;
import org.middleheaven.core.services.ServiceRegistry;

public class EmbeddedDSProvider implements DataSourceProvider{


	public static EmbeddedDSProvider provider(Properties properties){

		try {
			String  url = properties.getProperty("datasource.url");
			String username = properties.getProperty("datasource.username");
			String password = properties.getProperty("datasource.password");
			String catalog = properties.getProperty("datasource.catalog");
			return provider(catalog,new URL(url), username, password);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} 

	}

	/**
	 * Creates a in memory only volatile provider. The data stored where is deleted at 
	 * the of the application
	 * @param catalog
	 * @return
	 */
	public static EmbeddedDSProvider provider(String catalog){
		return new EmbeddedDSProvider(new EmbeddedDataSource(catalog, "sa", ""));
	}
	
	/**
	 * Creates a in memory only volatile provider. The data stored where is deleted at 
	 * the of the application
	 * @param catalog
	 * @param username
	 * @param password
	 * @return
	 */
	public static EmbeddedDSProvider provider(String catalog,String username,String password){
		return new EmbeddedDSProvider(new EmbeddedDataSource(catalog, username, password));
	}
	
	/**
	 * Creates a persisted data provider. The data stored where is storead in a file at the location of the URL.
	 * The URL must be writable from the application (normally a local disk URL).
	 * @param catalog
	 * @param url
	 * @return
	 */
	public static EmbeddedDSProvider provider(String catalog,URL url){
		return new EmbeddedDSProvider(new EmbeddedDataSource(url,catalog, "sa", ""));
	}
	
	public static EmbeddedDSProvider provider(String catalog,URL url, String username,String password){
		return new EmbeddedDSProvider(new EmbeddedDataSource(url,catalog, username, password));
	}


	EmbeddedDataSource ds;
	private boolean started;
	private EmbeddedDSProvider(EmbeddedDataSource ds) {
		this.ds = ds;
		ds.setAutoCommit(true);
		// register shutdownhook
		ServiceRegistry.getService(BootstrapService.class).addListener(new BootstapListener(){

			@Override
			public void onBoostapEvent(BootstrapEvent event) {
				if (event.isBootdown()){
					stop();
				}
			}
			
		});
	}

	@Override
	public DataSource getDataSource() {
		if (!started){
			this.start();
		}
		return ds;
	}

	public synchronized void start(){
		if (!started){
			started = true;
		}
		ds.start();
	}

	public synchronized void stop(){
		if (started){
			started = false;
		}
		ds.stop();
	}
}
