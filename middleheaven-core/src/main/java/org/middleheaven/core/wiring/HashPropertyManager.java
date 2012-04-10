/**
 * 
 */
package org.middleheaven.core.wiring;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 */
public class HashPropertyManager implements PropertyManager {

	
	private String name;

	private Map<String, Object> properties = new HashMap<String, Object>();
	
	public HashPropertyManager (String name){
		this.name = name;
	}
	
	/**
	 * {@inheritDoc}
	 *
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
		return properties.get(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsProperty(String key) {
		return properties.containsKey(key);
	}

	
	public void putProperty(String key, Object value){
		this.properties.put(key,value);
	}
}
