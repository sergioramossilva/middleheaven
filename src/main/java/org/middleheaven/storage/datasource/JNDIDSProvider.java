package org.middleheaven.storage.datasource;

import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;

import org.middleheaven.storage.StorageException;

public class JNDIDSProvider implements DataSourceProvider{

	
	public static JNDIDSProvider provider(Properties properties){

		String  url = properties.getProperty("datasource.url");
		url = url.substring(url.indexOf(':')+1);
		return provider(url);
	}
	
	public static JNDIDSProvider provider(String url){
		return new JNDIDSProvider(url);
	}

	String url;
	public JNDIDSProvider(String url){
		this.url = url;
	}
	
	DataSource ds;
	@Override
	public DataSource getDataSource() {
		if (ds==null){
			try {
				ds = lookup(url,DataSource.class);
			} catch (NamingException e) {
				throw new StorageException(e);
			}
		}
		return ds;
	}
	
	private <T> T lookup(String jndiName, Class<T> objectClass) throws NamingException {

		InitialContext context = new InitialContext();

		if (objectClass !=null){
			return objectClass.cast(PortableRemoteObject.narrow(context.lookup(jndiName), objectClass));
		} else {
			return objectClass.cast(context.lookup(jndiName));
		}
	}
}
