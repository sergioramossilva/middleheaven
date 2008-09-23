package org.middleheaven.storage.db.dialects;

import java.sql.SQLException;

import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.StorageException;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.storage.db.CriteriaInterpreter;
import org.middleheaven.storage.db.DataBaseDialect;

public class HSQLDialect extends DataBaseDialect{

	public HSQLDialect() {
		super("'", "'", ".");
	}
	
	public CriteriaInterpreter newCriteriaInterpreter(Criteria<?> criteria,
			StorableEntityModel model) {
		return new PostgressCriteriaInterpreter(this, criteria, model);
	}


	@Override
	public StorageException handleSQLException(SQLException e) {
		// TODO Auto-generated method stub
		return null;
	}


	private static class PostgressCriteriaInterpreter extends CriteriaInterpreter{

		public PostgressCriteriaInterpreter(DataBaseDialect dataBaseDialect,
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
}
