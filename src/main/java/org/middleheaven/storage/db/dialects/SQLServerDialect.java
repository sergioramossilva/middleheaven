package org.middleheaven.storage.db.dialects;

import java.sql.SQLException;

import org.middleheaven.storage.StorageException;
import org.middleheaven.storage.db.DataBaseDialect;

public class SQLServerDialect extends DataBaseDialect{

	public SQLServerDialect() {
		super("[", "]", ".");
	}

	@Override
	public StorageException handleSQLException(SQLException e) {
		// TODO Auto-generated method stub
		return null;
	}

}
