package org.middleheaven.storage.db.dialects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.middleheaven.domain.DataType;
import org.middleheaven.storage.QualifiedName;
import org.middleheaven.storage.StorableModelReader;
import org.middleheaven.storage.StorageException;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.storage.db.Clause;
import org.middleheaven.storage.db.ColumnModel;
import org.middleheaven.storage.db.ColumnValueHolder;
import org.middleheaven.storage.db.CriteriaInterpreter;
import org.middleheaven.storage.db.DataBaseDialect;
import org.middleheaven.storage.db.DataBaseModel;
import org.middleheaven.storage.db.DataBaseObjectModel;
import org.middleheaven.storage.db.DataBaseObjectType;
import org.middleheaven.storage.db.EditionDataBaseCommand;
import org.middleheaven.storage.db.RetriveDataBaseCommand;
import org.middleheaven.storage.db.SQLEditCommand;
import org.middleheaven.storage.db.SQLRetriveCommand;
import org.middleheaven.storage.db.SequenceModel;
import org.middleheaven.storage.db.SequenceSupportedDBDialect;
import org.middleheaven.storage.db.TableAlreadyExistsException;
import org.middleheaven.storage.db.TableModel;

public class HSQLDialect extends SequenceSupportedDBDialect{

	public HSQLDialect() {
		super("'", "'", ".");
	}
	
	protected  boolean supportsBatch(){
		return false;
	}
	
	// HSQL storedProcedures
	public static boolean containsMatch(String target, String search) {
		search = search.replaceAll("%", "");
		return target.toLowerCase().contains(search.toLowerCase());
	}

	public static boolean endsWithMatch(String target, String search) {
		search = search.replaceAll("%", "");
		return target.toLowerCase().endsWith((search.toLowerCase()));
	}
	
	public static boolean startsWithMatch(String target, String search) {
		search = search.replaceAll("%", "");
		return target.toLowerCase().startsWith((search.toLowerCase()));
	}
	// end stored procedures
	
	@Override
	public CriteriaInterpreter newCriteriaInterpreter(Criteria<?> criteria,
			StorableModelReader model) {
		return new HSQLCriteriaInterpreter(this, criteria, model);
	}
		
	@Override
	public StorageException handleSQLException(SQLException e) {
		if (e.getMessage().startsWith("Table already exists")){
			return new TableAlreadyExistsException();
		}
		return new StorageException(e);
	}
	
	@Override
	public void writeJoinTableHardname(Clause joinClause, String hardNameForEntity) {
		joinClause.append(hardNameForEntity);
	}


	@Override
	public void writeJoinField(Clause joinClause, String alias ,String fieldName) {
		joinClause.append(alias).append(fieldSeparator()).append(fieldName);
	}
	
	@Override
	public EditionDataBaseCommand createCreateSequenceCommand(SequenceModel sequence) {

		StringBuilder sql = new StringBuilder("CREATE SEQUENCE ")
		.append(hardSequenceName(sequence.getName())) 
		.append(" AS INTEGER ")
		.append(" START WITH " ).append(sequence.getStartWith())
		.append(" INCREMENT BY ").append(sequence.getIncrementBy());

		return new SQLEditCommand(this,sql.toString());
	}

	@Override
	public void writeEditionHardname(Clause buffer , QualifiedName hardname){
		if (!hardname.getName().isEmpty()){
			buffer.append(hardname.getName().toLowerCase());
		}
	}
	
	@Override
	public void writeEnclosureHardname(Clause buffer , String hardname){
		buffer.append(hardname);
	}
	
	@Override
	public void writeQueryHardname(Clause buffer , QualifiedName hardname){
		if (hardname.isAlias()){
			buffer.append(hardname.getQualifier().toLowerCase())
			.append(fieldSeparator())
			.append(hardname.getName().toLowerCase());
		} else {
			buffer.append(hardname.getName().toLowerCase());
		}
	}


	private static class HSQLCriteriaInterpreter extends CriteriaInterpreter{

		public HSQLCriteriaInterpreter(DataBaseDialect dataBaseDialect,
				Criteria<?> criteria, StorableModelReader reader) {
			super(dataBaseDialect, criteria, reader);
		}
		
		@Override
		protected void writeFromClause(String alias , Clause queryBuffer){

			// FROM ClAUSE
			queryBuffer.append(" FROM ");
			queryBuffer.append(model().getEntityHardName().toLowerCase());

			if (alias!=null){
				queryBuffer.append(" AS ") 
				.append(alias);
			}
		}
		
		
		protected void writeStartLimitClause(Clause selectBuffer){
			if (criteria().isDistinct()){
				selectBuffer.append(" DISTINCT ");
			} 
			
			if (!criteria().isCountOnly() && criteria().getCount()>0){
				int offset = 0;
				if (criteria().getStart()>1){
					offset = criteria().getStart()-1;
				} 
				
				selectBuffer.append(" LIMIT ").append(offset).append(" ").append(criteria().getCount());
				
			}
		}
	}
	

	@Override
	protected <T> RetriveDataBaseCommand createNextSequenceValueCommand(String sequenceName) {
		return new SQLRetriveCommand( this,
				new StringBuilder("SELECT NEXT VALUE FOR ")
				.append(hardSequenceName(sequenceName))
				.append(" FROM INFORMATION_SCHEMA.SYSTEM_SEQUENCES")
				.toString(),
				Collections.<ColumnValueHolder>emptySet()
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

	public EditionDataBaseCommand createCreateTableCommand(TableModel tm){

		Clause sql = new Clause("CREATE CACHED TABLE ");
		sql.append(tm.getName());
		sql.append("(\n ");
		for (ColumnModel cm : tm){
			sql.append(cm.getName());
			sql.append(" ");
			appendNativeTypeFor(sql , cm);
			if(!cm.isNullable()){
				sql.append(" NOT ");
			} 
			sql.append(" NULL ");
			if (cm.isKey()){
				sql.append("PRIMARY KEY");
			} 
			sql.append(",\n");
		}
		sql.removeLastCharacters(2);
		sql.append(")");
		return new SQLEditCommand(this,sql.toString());
	}
	
	@Override
	protected void appendNativeTypeFor(Clause sql, ColumnModel column) {
		switch (column.getType()){
		case DATE:
		case DATETIME:
		case TIME:
			sql.append("datetime");
			break;
		case TEXT:
			sql.append("varchar (").append(column.getSize()).append(")");
			break;
		case MEMO:
			sql.append("varchar");
			break;
		case INTEGER:
			sql.append("bigint");
			break;
		case LOGIC:
		case ENUM:
			sql.append("int");
			break;
		case DECIMAL:
			sql.append("numeric (").append(column.getPrecision()).append(",").append(column.getSize()).append(")");
			break;
		default:
			throw new StorageException(column.getType() + " is not convertible to a native column type");
		}

	}

	public DataBaseModel readDataBaseModel(String catalog, DataSource ds) {
		Connection con=null;
		try{
			con = ds.getConnection();
	
			DataBaseModel dbm = new DataBaseModel();
			
			PreparedStatement psTables = con.prepareStatement("SELECT table_name FROM INFORMATION_SCHEMA.SYSTEM_TABLES WHERE TABLE_SCHEM = 'PUBLIC' AND TABLE_TYPE = 'TABLE'");
			PreparedStatement psColumns = con.prepareStatement("SELECT column_name,data_type, column_size, nullable, decimal_digits FROM INFORMATION_SCHEMA.SYSTEM_COLUMNS WHERE TABLE_SCHEM = 'PUBLIC' AND TABLE_NAME = ? ");
			

			ResultSet tables = psTables.executeQuery();
			while (tables.next()) {
			
					TableModel tm = new TableModel(tables.getString(1));

					psColumns.setString(1, tm.getName());

					ResultSet columns = psColumns.executeQuery();
					while (columns.next()) {

						DataType type = this.typeFromNative(columns.getInt(2));
						ColumnModel col = new ColumnModel(columns.getString(1), type);
						if (type.isTextual()){
							col.setSize(columns.getInt(3));
						}
						if (type.isDecimal()){
							col.setPrecision((columns.getInt(5)));
							col.setSize(columns.getInt(3));
						}

						col.setNullable(columns.getInt(4) == 1 );
						tm.addColumn(col);
					}

					dbm.addDataBaseObjectModel(tm);
				
			}

			tables.close();

			PreparedStatement psSequences = con.prepareStatement("SELECT  sequence_name , start_with , increment  FROM INFORMATION_SCHEMA.SYSTEM_SEQUENCES WHERE SEQUENCE_SCHEMA = 'PUBLIC'");
			
			ResultSet sequences = psSequences.executeQuery();
			while (sequences.next()) {
				SequenceModel sm = new SequenceModel(logicSequenceName(sequences.getString(1)) , sequences.getInt(2), sequences.getInt(3));
				dbm.addDataBaseObjectModel(sm);
			}
			
			psSequences.close();
			
			return dbm;
		} catch (SQLException e) {
			throw this.handleSQLException(e);
		} finally {
			if(con!=null){
				try {
					con.close();
				} catch (SQLException e) {
					throw this.handleSQLException(e);
				}
			}
		}
	}
}
