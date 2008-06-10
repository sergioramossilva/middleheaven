package org.middleheaven.storage.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLDataBaseCommand implements DataBaseCommand {

	private String sql;
	public SQLDataBaseCommand(String sql){
		this.sql = sql;
	}
	
	@Override
	public PreparedStatement getStatement(Connection con) throws SQLException {
		return con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY , ResultSet.CONCUR_READ_ONLY);
	}

}
