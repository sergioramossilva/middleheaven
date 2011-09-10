package org.middleheaven.storage.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class SQLRetriveCommand extends SQLConditionableCommand implements RetriveDataBaseCommand {


    private PreparedStatement ps;
	
	public SQLRetriveCommand(DataBaseDialect dialect,String sql,Collection<ColumnValueHolder> data){
		super(dialect,sql,data);
	}

	@Override
	public ResultSet getResult() throws SQLException {
		return ps.executeQuery();
	}

	@Override
	public boolean execute(DataBaseStorage storage, Connection connection) throws SQLException {

	    ps = super.prepareStatement(storage,connection);
	   
	    return true;
	}

	

}
