package org.middleheaven.storage.db.dialects;

import java.sql.SQLException;

import org.middleheaven.storage.StorageException;
import org.middleheaven.storage.db.SequenceNotSupportedDialect;

public class SQLServerDialect extends SequenceNotSupportedDialect{

	public SQLServerDialect() {
		super("[", "]", ".");
	}

	@Override
	public StorageException handleSQLException(SQLException e) {
		// TODO Auto-generated method stub
		return null;
	}

}
