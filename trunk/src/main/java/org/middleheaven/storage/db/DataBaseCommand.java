package org.middleheaven.storage.db;

import java.sql.Connection;
import java.sql.SQLException;


public interface DataBaseCommand {

	/**
	 * 
	 * @return dialect object that created this command
	 */
	public DataBaseDialect getDialect();
	
	/**
	 * 
	 * @param storage
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public boolean execute(DataBaseStorage storage, Connection connection)  throws SQLException;

}
