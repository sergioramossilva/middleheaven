package org.middleheaven.persistance.db;

/**
 * Indicates a table already exists in the RDBMS internal model.
 */
public class TableAlreadyExistsException extends RDBMSException {

	private static final long serialVersionUID = -2767431618823638249L;

	/**
	 * 
	 * Constructor.
	 */
	public TableAlreadyExistsException() {
		super("Table already exists");
	}

}
