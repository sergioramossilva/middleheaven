package org.middleheaven.storage.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface StoreCommandContext {

	public DataBaseStorage getStorage();
	
	public Connection getConnection() throws SQLException;

	
}
