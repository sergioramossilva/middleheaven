/**
 * 
 */
package org.middleheaven.core.wiring;

/**
 * 
 */
public class SytemEnvPropertyManager implements PropertyManager {

	
	public SytemEnvPropertyManager (){}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return "system.env"
	 */
	@Override
	public String getName() {
		return "system.env";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getProperty(String key) {
		return System.getenv(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsProperty(String key) {
		return System.getenv().containsKey(key);
	}

}
