package org.middleheaven.storage.db.dialects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;

import javax.sql.DataSource;

import org.middleheaven.data.DataType;
import org.middleheaven.storage.QualifiedName;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.StorageException;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.storage.criteria.FieldValueHolder;
import org.middleheaven.storage.db.ColumnModel;
import org.middleheaven.storage.db.CriteriaInterpreter;
import org.middleheaven.storage.db.DataBaseDialect;
import org.middleheaven.storage.db.DataBaseModel;
import org.middleheaven.storage.db.EditionDataBaseCommand;
import org.middleheaven.storage.db.RetriveDataBaseCommand;
import org.middleheaven.storage.db.SQLEditCommand;
import org.middleheaven.storage.db.SQLRetriveCommand;
import org.middleheaven.storage.db.SequenceSupportedDBDialect;
import org.middleheaven.storage.db.TableAlreadyExistsException;
import org.middleheaven.storage.db.TableModel;

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
		if (e.getMessage().startsWith("Table already exists")){
			return new TableAlreadyExistsException();
		}
		return new StorageException(e);
	}

	public void writeEditionHardname(StringBuilder buffer , QualifiedName hardname){

		if (!hardname.getColumnName().isEmpty()){
			buffer.append(hardname.getColumnName().toLowerCase());
		}
	}

	

	public void writeQueryHardname(StringBuilder buffer , QualifiedName hardname){
		buffer.append(hardname.getColumnName().toLowerCase());
	}


	private static class HSQLCriteriaInterpreter extends CriteriaInterpreter{

		public HSQLCriteriaInterpreter(DataBaseDialect dataBaseDialect,
				Criteria<?> criteria, StorableEntityModel model) {
			super(dataBaseDialect, criteria, model);
		}
		
		protected void writeFromClause(StringBuilder queryBuffer){

			// FROM ClAUSE
			queryBuffer.append(" FROM ");
			queryBuffer.append(model().getEntityHardName().toLowerCase());


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

	public EditionDataBaseCommand createCreateTableCommand(TableModel tm){

		StringBuilder sql = new StringBuilder("CREATE CACHED TABLE ");
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
		sql.delete(sql.length()-2, sql.length());
		sql.append(")");
		return new SQLEditCommand(sql.toString());
	}
	
	@Override
	protected void appendNativeTypeFor(StringBuilder sql, ColumnModel column) {
		switch (column.getType()){
		case DATE:
		case DATETIME:
		case TIME:
			sql.append("datetime");
			break;
		case TEXT:
			sql.append("varchar (").append(column.getSize()).append(")");
			break;
		case INTEGER:
		case LOGIC:
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

					dbm.addTable(tm);
				
			}

			tables.close();

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
