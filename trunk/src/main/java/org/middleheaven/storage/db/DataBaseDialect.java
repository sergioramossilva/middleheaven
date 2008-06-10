package org.middleheaven.storage.db;

import java.sql.SQLException;

import org.middleheaven.storage.QualifiedName;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.StorageException;
import org.middleheaven.storage.criteria.Criteria;

public abstract class DataBaseDialect {

	private final String startDelimiter;
	private final String endDelimiter;
	private final String fieldSeparator;


	protected DataBaseDialect(String startDelimiter, String endDelimiter,String fieldSeparator) {

		this.startDelimiter = startDelimiter;
		this.endDelimiter = endDelimiter;
		this.fieldSeparator = fieldSeparator;
	}

	public String startDelimiter() {
		return startDelimiter;
	}

	public String endDelimiter() {
		return endDelimiter;
	}
	
	public String fieldSeparator() {
		return fieldSeparator;
	}
	
	public void writeHardname(StringBuilder buffer , QualifiedName hardname ){
		buffer.append(startDelimiter);
		buffer.append(hardname.getTableName().toLowerCase());
		buffer.append(endDelimiter);
		buffer.append(fieldSeparator);
		buffer.append(startDelimiter);
		buffer.append(hardname.getColumnName().toLowerCase());
		buffer.append(endDelimiter);
	}
	
	public abstract StorageException handleSQLException(SQLException e);

	public final <T> DataBaseCommand createSelectCommand (Criteria<T> criteria, StorableEntityModel model ){
		
		return new SQLDataBaseCommand(newCriteriaInterpreter(criteria,model).translate());
	}


	public  DataBaseCommand createInsertCommand(StorableEntityModel model){}

	public  DataBaseCommand createDeleteCommand(StorableEntityModel model){}

	public  DataBaseCommand createUpdateCommand(StorableEntityModel model){}

	public CriteriaInterpreter newCriteriaInterpreter(Criteria<?> criteria,
			StorableEntityModel model) {
		return new CriteriaInterpreter(this, criteria, model);
		
	}


}
