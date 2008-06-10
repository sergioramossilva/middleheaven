package org.middleheaven.storage.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface DataBaseCommand {

	PreparedStatement getStatement(Connection con)  throws SQLException;
}
