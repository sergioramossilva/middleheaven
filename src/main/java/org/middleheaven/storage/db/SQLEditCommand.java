package org.middleheaven.storage.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.middleheaven.storage.StorableEntityModel;

public class SQLEditCommand implements EditionDataBaseCommand {

	String sql;
	public SQLEditCommand(String sql) {
		super();
		this.sql = sql;
	}
	
	@Override
	public boolean execute(Connection con, StorableEntityModel model) throws SQLException {
		
		PreparedStatement ps = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY , ResultSet.CONCUR_READ_ONLY);
		return ps.executeUpdate()>0;
	}
	
	public String toString(){
		return sql;
	}

}
