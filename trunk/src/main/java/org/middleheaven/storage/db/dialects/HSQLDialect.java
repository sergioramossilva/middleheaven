package org.middleheaven.storage.db.dialects;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;


import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.StorageException;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.storage.criteria.FieldValueHolder;
import org.middleheaven.storage.db.ColumnModel;
import org.middleheaven.storage.db.CriteriaInterpreter;
import org.middleheaven.storage.db.DataBaseDialect;
import org.middleheaven.storage.db.RetriveDataBaseCommand;
import org.middleheaven.storage.db.SQLRetriveCommand;
import org.middleheaven.storage.db.SequenceSupportedDBDialect;

public class HSQLDialect extends SequenceSupportedDBDialect{

	public HSQLDialect() {
		super("'", "'", ".");
	}
	
	public CriteriaInterpreter newCriteriaInterpreter(Criteria<?> criteria,
			StorableEntityModel model) {
		return new HSQLCriteriaInterpreter(this, criteria, model);
	}


	@Override
	public StorageException handleSQLException(SQLException e) {
		// TODO Auto-generated method stub
		return null;
	}


	private static class HSQLCriteriaInterpreter extends CriteriaInterpreter{

		public HSQLCriteriaInterpreter(DataBaseDialect dataBaseDialect,
				Criteria<?> criteria, StorableEntityModel model) {
			super(dataBaseDialect, criteria, model);
		}
		
		protected void writeEndLimitClause(StringBuffer selectBuffer){
			if (criteria().getCount()>0){
				selectBuffer.append(" LIMIT ").append(criteria().getCount());
				if (criteria().getStart()>1){
					selectBuffer.append(" OFFSET ").append(criteria().getStart()-1);
				}
			}
		}
	}
	

	@Override
	protected <T> RetriveDataBaseCommand createNextSequenceValueCommand(String sequenceName) {
		final Collection<FieldValueHolder> none = Collections.emptySet();
		return new SQLRetriveCommand(
				new StringBuilder("SELECT NEXT VALUE FOR ")
				.append(sequenceName)
				.toString(),
				none
		);
	}

	@Override
	public boolean supportsCountLimit() {
		return true;
	}

	@Override
	public boolean supportsOffSet() {
		return true;
	}

	@Override
	protected void appendNativeTypeFor(StringBuilder sql, ColumnModel type) {
		// TODO implement HSQLDialect.appendNativeTypeFor
		
	}
}
