package org.middleheaven.storage.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.middleheaven.storage.StorableEntityModel;

public interface DataBaseCommand {

	public DataBaseDialect getDialect();
	
	public boolean execute(DataBaseStorage keeper,Connection con, StorableEntityModel model)  throws SQLException;

}
