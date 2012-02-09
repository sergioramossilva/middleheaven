/**
 * 
 */
package org.middleheaven.persistance;

/**
 * 
 */
public class DataStoreSchemaNotFoundException extends RuntimeException {


	private static final long serialVersionUID = 4242082041000300643L;

	public DataStoreSchemaNotFoundException() {
		super();
	}
	
	/**
	 * Constructor.
	 * @param e
	 */
	public DataStoreSchemaNotFoundException(Throwable cause) {
		super(cause);
	}

}
