package org.middleheaven.storage.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.middleheaven.sequence.Sequence;



public abstract class SequenceNotSupportedDBDialect extends DataBaseDialect implements TableBaseSequenceListener {

	protected SequenceNotSupportedDBDialect(String startDelimiter,
			String endDelimiter, String fieldSeparator) {
		super(startDelimiter, endDelimiter, fieldSeparator);
	}

	private final Map<String, TableBasedSequence> sequences = new TreeMap<String, TableBasedSequence>();
		
	@Override
	public Sequence<Long> getSequence(DataSource ds, String identifiableName) {
		TableBasedSequence sequence = sequences.get(identifiableName);
		if ( sequence==null){
			sequence = new TableBasedSequence(ds,identifiableName);
			loadTableSequence(ds,sequence);
			sequence.addSequenceListener(this);
			sequences.put(identifiableName, sequence);
		}
		return sequence;
	}
	

	public final void onSequenceFoward(TableBasedSequence tableSequence){
		storeTableSequence(tableSequence.getDataSource(), tableSequence);
	}
	
	protected void loadTableSequence(DataSource ds ,TableBasedSequence sequence){
		Connection con =null;
		try {
			con = ds.getConnection();
			RetriveDataBaseCommand command = createSequenceStateReadCommand(sequence);
			
			command.execute(null,con);
			ResultSet rs = command.getResult();
			
			if (rs.next()){
				sequence.setLastUsed(rs.getInt(1));
			} else {
				sequence.setLastUsed(0);
			}
			
		} catch (SQLException e){
			throw handleSQLException(e);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				throw handleSQLException(e);
			}
		}
	}
	
	protected void storeTableSequence(DataSource ds ,TableBasedSequence sequence){
		Connection con =null;
		try {
			con = ds.getConnection();
			DataBaseCommand command = createUpdateSequenceValueCommand(sequence, sequence.getLastUsed());
			
			if (!command.execute(null, con)){
				//sequence does not exist 
				DataBaseCommand insertCommand = createInsertSequenceValueCommand(sequence);
				insertCommand.execute(null, con);
			}
			command.execute(null, con);
		} catch (SQLException e){
			throw handleSQLException(e);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				throw handleSQLException(e);
			}
		}
	}
	
	protected abstract DataBaseCommand createUpdateSequenceValueCommand(TableBasedSequence sequence,long lastUsed);
	protected abstract DataBaseCommand createInsertSequenceValueCommand(TableBasedSequence sequence);
	
	protected abstract RetriveDataBaseCommand  createSequenceStateReadCommand(TableBasedSequence sequence);
	
}
