package org.middleheaven.persistance.db.dialects;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.middleheaven.persistance.PersistanceException;
import org.middleheaven.persistance.db.RDBMSDialect;
import org.middleheaven.persistance.db.RetriveDataBaseCommand;
import org.middleheaven.persistance.db.metamodel.DataBaseObjectModel;
import org.middleheaven.persistance.db.metamodel.DataBaseObjectType;
import org.middleheaven.persistance.db.metamodel.EditableDataBaseModel;
import org.middleheaven.persistance.db.metamodel.SequenceModel;
import org.middleheaven.sequence.DefaultToken;
import org.middleheaven.sequence.Sequence;
import org.middleheaven.sequence.SequenceToken;


public abstract class SequenceSupportedDBDialect extends RDBMSDialect {

	protected SequenceSupportedDBDialect(String startDelimiter,
			String endDelimiter, String fieldSeparator) {
		super(startDelimiter, endDelimiter, fieldSeparator);
	}

	@Override
	public void extendsDatabaseModel(EditableDataBaseModel model){

		List<SequenceModel> sequences = new LinkedList<SequenceModel>();
		
		for (DataBaseObjectModel table : model){
			if(table.getType().equals(DataBaseObjectType.TABLE)){
				sequences.add(new SequenceModel(table.getName() , 0,1));
			}
		}
		
		for (SequenceModel seq : sequences){
			model.addDataBaseObjectModel(seq);
		}
	}
	
	@Override
	public final Sequence<Long> getSequence(DataSource ds, String identifiableName) {
		
		return new NativeIntegerSequence(this , ds,identifiableName);
	}

	private static class NativeIntegerSequence implements Sequence<Long> {
		private DataSource ds;
		private String identifiableName;
		private SequenceSupportedDBDialect dialect;
		
		public NativeIntegerSequence(SequenceSupportedDBDialect dialect , DataSource ds, String identifiableName) {
			super();
			this.dialect = dialect;
			this.ds = ds;
			this.identifiableName = identifiableName;
		}

		@Override
		public SequenceToken<Long> next() {
			Connection con = null;
			try {
				con = ds.getConnection();
				RetriveDataBaseCommand command = dialect.createNextSequenceValueCommand(identifiableName);
				
				command.execute(null,con, null);
				ResultSet rs = command.getResult();
				
				if (rs.next()){
					return new DefaultToken<Long>(rs.getLong(1));
				} 
				throw new PersistanceException("Cannot read next value for native sequence '" + identifiableName + "'");
			} catch (SQLException e){
				throw dialect.handleSQLException(e);
			} finally {
				try {
					con.close();
				} catch (SQLException e) {
					throw dialect.handleSQLException(e);
				}
			}
		}
		
	}

	protected abstract <T> RetriveDataBaseCommand createNextSequenceValueCommand (String sequenceName);
}
