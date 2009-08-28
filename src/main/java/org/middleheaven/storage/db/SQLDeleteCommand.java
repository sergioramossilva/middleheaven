package org.middleheaven.storage.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

public class SQLDeleteCommand extends SQLConditionableCommand {

	protected SQLDeleteCommand(DataBaseDialect dialect,String sql, Collection<ColumnValueHolder> data) {
		super(dialect,sql, data);
	}

	@Override
	public boolean execute(DataBaseStorage storage, Connection connection) throws SQLException {

		return prepareStatement(storage,connection).execute();


	}
}
