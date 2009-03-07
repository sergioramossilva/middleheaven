package org.middleheaven.storage.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.middleheaven.sequence.DefaultToken;
import org.middleheaven.sequence.Sequence;
import org.middleheaven.sequence.SequenceToken;
import org.middleheaven.storage.StorageException;


public abstract class SequenceSupportedDBDialect extends DataBaseDialect {

	protected SequenceSupportedDBDialect(String startDelimiter,
			String endDelimiter, String fieldSeparator) {
		super(startDelimiter, endDelimiter, fieldSeparator);
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
			Connection con =null;
			try {
				con = ds.getConnection();
				RetriveDataBaseCommand command = dialect.createNextSequenceValueCommand(identifiableName);
				
				command.execute(con, null);
				ResultSet rs = command.getResult();
				
				if (rs.next()){
					return new DefaultToken<Long>(rs.getLong(1));
				} 
				throw new StorageException("Cannot read next value for native sequence '" + identifiableName + "'");
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
