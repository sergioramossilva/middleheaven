package org.middleheaven.storage.db;

import java.sql.Connection;
import java.sql.SQLException;

public class DefaultStoreCommandContext implements StoreCommandContext {

	private Connection connection;
	private DataBaseStorage dataBaseStorage;

	public DefaultStoreCommandContext(Connection connection,
			DataBaseStorage dataBaseStorage) {
		super();
		this.connection = connection;
		this.dataBaseStorage = dataBaseStorage;
	}

	@Override
	public Connection getConnection() throws SQLException {
		return connection;
	}

	@Override
	public DataBaseStorage getStorage() {
		return dataBaseStorage;
	}

}
