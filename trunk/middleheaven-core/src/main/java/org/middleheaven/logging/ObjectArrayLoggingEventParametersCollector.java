/**
 * 
 */
package org.middleheaven.logging;

/**
 * 
 */
public class ObjectArrayLoggingEventParametersCollector implements
		LoggingEventParametersCollector {

	
	private Object[] params;
	
	public ObjectArrayLoggingEventParametersCollector (Object[] params){
		this.params = params;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] collect() {
		return params;
	}

}
