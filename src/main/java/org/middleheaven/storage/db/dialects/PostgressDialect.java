package org.middleheaven.storage.db.dialects;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;


import org.middleheaven.storage.QualifiedName;
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

public class PostgressDialect extends SequenceSupportedDBDialect{

	public PostgressDialect() {
		super("'", "'", ".");
	}

	public CriteriaInterpreter newCriteriaInterpreter(Criteria<?> criteria,
			StorableEntityModel model) {
		return new PostgressCriteriaInterpreter(this, criteria, model);
	}


	@Override
	public StorageException handleSQLException(SQLException e) {
		String msg = e.getMessage();
		if (e.getNextException()!=null){
			msg += "\n" + e.getNextException().getMessage();
		}
		return new StorageException(msg);
	}

	public void writeQueryHardname(StringBuilder buffer , QualifiedName hardname){


		buffer.append(hardname.getTableName().toLowerCase());
		buffer.append(fieldSeparator());
		buffer.append(hardname.getColumnName().toLowerCase());
	}
	
	public void writeEditionHardname(StringBuilder buffer , QualifiedName hardname){

		buffer.append(hardname.getColumnName().toLowerCase());

	}

	private static class PostgressCriteriaInterpreter extends CriteriaInterpreter{

		public PostgressCriteriaInterpreter(DataBaseDialect dataBaseDialect,
				Criteria<?> criteria, StorableEntityModel model) {
			super(dataBaseDialect, criteria, model);
		}

		protected void writeFromClause(StringBuilder queryBuffer){

			// FROM ClAUSE
			queryBuffer.append(" FROM ");
			queryBuffer.append(model().getEntityHardName().toLowerCase());

		}
		
		protected void writeEndLimitClause(StringBuilder selectBuffer){
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
				new StringBuilder("SELECT nextval('")
				.append(sequenceName)
				.append("') as sequenceValue")
				.toString() ,
				none
		);
	}

	@Override
	protected void appendNativeTypeFor(StringBuilder sql, ColumnModel type) {
		// TODO implement PostgressDialect.appendNativeTypeFor
		
	}
	
	@Override
	public boolean supportsCountLimit() {
		return true;
	}

	@Override
	public boolean supportsOffSet() {
		return true;
	}

}
