package org.middleheaven.storage.db.dialects;

import java.sql.Connection;
import java.sql.SQLException;

import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.storage.db.CriteriaInterpreter;
import org.middleheaven.storage.db.DataBaseDialect;
import org.middleheaven.storage.db.SequenceSupportedDialect;

public class HSQLDialect extends SequenceSupportedDialect{

	public HSQLDialect() {
		super("'", "'", ".");
	}
	
	public CriteriaInterpreter newCriteriaInterpreter(Criteria<?> criteria,
			StorableEntityModel model) {
		return new HSQLCriteriaInterpreter(this, criteria, model);
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
	
	public void lastCommand(Connection con) throws SQLException{
		con.prepareStatement("SHUTDOWN").execute();
	}
}
