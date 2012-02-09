package org.middleheaven.persistance.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import org.middleheaven.persistance.DataRow;
import org.middleheaven.persistance.db.mapping.DataBaseMapper;

/**
 * 
 */
public class SQLDeleteCommand extends SQLConditionableCommand {

	/**
	 * 
	 * Constructor.
	 * @param dialect
	 * @param sql
	 * @param data
	 */
	protected SQLDeleteCommand(RDBMSDialect dialect,String sql, Collection<ValueHolder> values) {
		super(dialect, sql, values);
	}

	@Override
	public boolean execute(DataBaseMapper storage, Connection connection, QueryParameters parameters) throws SQLException {

		return prepareStatement(storage,connection,parameters).execute();


	}


}
