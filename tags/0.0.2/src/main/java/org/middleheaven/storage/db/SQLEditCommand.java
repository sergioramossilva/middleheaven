package org.middleheaven.storage.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SQLEditCommand implements EditionDataBaseCommand {

	final private String sql;
	final private DataBaseDialect dialect;
	
	public SQLEditCommand(DataBaseDialect dialect , String sql) {
		this.dialect = dialect;
		this.sql = sql;
	}
	
	@Override
	public boolean execute(DataBaseStorage storage, Connection connection) throws SQLException {
		
		PreparedStatement ps = connection.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY , ResultSet.CONCUR_READ_ONLY);
		return ps.executeUpdate()>0;
	}
	
	public String toString(){
		return sql;
	}

	@Override
	public DataBaseDialect getDialect() {
		return dialect;
	}

	@Override
	public String getSQL() {
		return sql;
	}

}
