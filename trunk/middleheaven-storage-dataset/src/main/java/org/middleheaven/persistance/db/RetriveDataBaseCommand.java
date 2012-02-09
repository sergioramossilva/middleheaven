package org.middleheaven.persistance.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RetriveDataBaseCommand extends ConditionableDataBaseCommand {

	public ResultSet getResult() throws SQLException; 
}
