package org.middleheaven.storage.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.criteria.FieldValueHolder;

public class SQLRetriveCommand extends SQLConditionableCommand implements RetriveDataBaseCommand {


    private PreparedStatement ps;
	
	protected SQLRetriveCommand(String sql,Collection<FieldValueHolder> data){
		super(sql,data);
	}

	@Override
	public ResultSet getResult() throws SQLException {
		return ps.executeQuery();
	}

	@Override
	public void execute(Connection con,StorableEntityModel model) throws SQLException {

	    ps = super.prepareStatement(con);
	   
	}

	

}
