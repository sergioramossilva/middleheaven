/**
 * 
 */
package org.middleheaven.persistance;

import java.sql.SQLException;

/**
 * 
 */
public class DataAccessException extends RuntimeException {

	/**
	 * Constructor.
	 * @param e
	 */
	public DataAccessException(SQLException e) {
		super(e);
	}

}
