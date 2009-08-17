package org.middleheaven.storage.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.middleheaven.storage.criteria.FieldValueHolder;

public abstract class SQLConditionableCommand implements ConditionableDataBaseCommand {

	private final String sql;
	private final Collection<FieldValueHolder> data;
	private DataBaseDialect dialect;

	protected SQLConditionableCommand(DataBaseDialect dialect,String sql,Collection<FieldValueHolder> data){
		this.dialect = dialect;
		this.data = data;
		this.sql = sql;
	}

	public DataBaseDialect getDialect(){
		return dialect;
	}
	
	protected final PreparedStatement prepareStatement(DataBaseStorage keeper, Connection con) throws SQLException{
		PreparedStatement ps = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY , ResultSet.CONCUR_READ_ONLY);

		PreparedStatementStorable pss = new PreparedStatementStorable(keeper,ps);

		int param = 1;
		for (FieldValueHolder vh : data){
			pss.setField(param, vh.getValue(), vh.getDataType());
			param++;
		}

		return ps;
	}
	
	public String toString(){
		return sql;
	}

}
