/**
 * 
 */
package org.middleheaven.core.wiring;

/**
 * 
 */
public class SytemPropertyManager implements PropertyManager {

	
	public SytemPropertyManager (){}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return "system.properties"
	 */
	@Override
	public String getName() {
		return "system.properties";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getProperty(String key) {
		return System.getProperty(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsProperty(String key) {
		return System.getProperties().contains(key);
	}

}
