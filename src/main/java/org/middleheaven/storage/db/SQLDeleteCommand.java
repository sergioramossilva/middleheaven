package org.middleheaven.storage.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.criteria.FieldValueHolder;

public class SQLDeleteCommand extends SQLConditionableCommand {

	protected SQLDeleteCommand(String sql, Collection<FieldValueHolder> data) {
		super(sql, data);
	}

	@Override
	public void execute(Connection con,StorableEntityModel model) throws SQLException {

		prepareStatement(con).execute();


	}
}
