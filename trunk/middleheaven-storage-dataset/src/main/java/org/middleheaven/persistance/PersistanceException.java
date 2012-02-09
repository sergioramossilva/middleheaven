/**
 * 
 */
package org.middleheaven.persistance;

import java.sql.SQLException;

/**
 * 
 */
public class PersistanceException extends RuntimeException{

	private static final long serialVersionUID = -6292002542020120951L;
	
	/**
	 * Constructor.
	 * @param string
	 */
	public PersistanceException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param e
	 */
	public PersistanceException(Exception cause) {
		super(cause);
	}



}
