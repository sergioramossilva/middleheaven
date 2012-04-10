/**
 * 
 */
package org.middleheaven.core.wiring;

import java.io.IOException;
import java.util.Properties;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;

/**
 * 
 */
public class PropertiesPropertyManager implements PropertyManager{

	
	
	public static PropertiesPropertyManager loadFrom(ManagedFile file) throws ManagedIOException{
		return loadFrom(file.getPath().getFileNameWithoutExtension(), file);
	}
	
	public static PropertiesPropertyManager loadFrom(String name, ManagedFile file) throws ManagedIOException{

	
		try {
			Properties properties = new Properties();
			
			if (file.exists()) {
				properties.load(file.getContent().getInputStream());
			}
			
			return new PropertiesPropertyManager(name, properties);
		} catch (IOException e) {
			throw ManagedIOException.manage(e);
		}
	}
	
	public static PropertiesPropertyManager loadFrom(String name, Properties properties ){
		return new PropertiesPropertyManager(name, properties);
	}

	private String name;
	private Properties properties;
	
	public PropertiesPropertyManager (String name, Properties properties){
		this.name = name;
		this.properties = properties;
		
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getProperty(String key) {
		return properties.getProperty(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsProperty(String key) {
		return properties.containsKey(key);
	}

}
