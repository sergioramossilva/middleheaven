package org.middleheaven.persistance.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.middleheaven.persistance.db.mapping.DataBaseMapper;


/**
 * 
 */
public class SQLEditCommand implements EditionDataBaseCommand {

	private final String sql;
	private final RDBMSDialect dialect;
	
	/**
	 * 
	 * Constructor.
	 * @param dialect
	 * @param sql
	 */
	public SQLEditCommand(RDBMSDialect dialect , String sql) {
		this.dialect = dialect;
		this.sql = sql;
	}
	
	@Override
	public boolean execute(DataBaseMapper storage, Connection connection, QueryParameters parameters) throws SQLException {
		
		PreparedStatement ps = connection.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY , ResultSet.CONCUR_READ_ONLY);
		return ps.executeUpdate()>0;
	}
	
	public String toString(){
		return sql;
	}

	@Override
	public RDBMSDialect getDialect() {
		return dialect;
	}

	@Override
	public String getSQL() {
		return sql;
	}

}
