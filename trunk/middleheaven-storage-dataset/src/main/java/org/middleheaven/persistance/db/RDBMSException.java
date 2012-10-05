package org.middleheaven.persistance.db;

/**
 * Layer Exception for RDBMS data set provider.
 */
public class RDBMSException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * Constructor.
	 * @param cause the cause of the exception.
	 */
	public RDBMSException(Throwable cause){
		super(cause);
	}
	
	/**
	 * 
	 * Constructor.
	 * @param cause the cause of the exception.
	 */
	public RDBMSException(String cause){
		super(cause);
	}
}
