package org.middleheaven.storage.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.middleheaven.data.DataType;
import org.middleheaven.sequence.Sequence;
import org.middleheaven.storage.QualifiedName;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.StorableFieldModel;
import org.middleheaven.storage.StorageException;
import org.middleheaven.storage.criteria.AbstractCriteria;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.storage.criteria.Criterion;
import org.middleheaven.storage.criteria.FieldCriterion;
import org.middleheaven.storage.criteria.LogicCriterion;

public abstract class DataBaseDialect {

	private final String startDelimiter;
	private final String endDelimiter;
	private final String fieldSeparator;


	protected DataBaseDialect(String startDelimiter, String endDelimiter,String fieldSeparator) {

		this.startDelimiter = startDelimiter;
		this.endDelimiter = endDelimiter;
		this.fieldSeparator = fieldSeparator;
	}

	/**
	 * 
	 * @return true if this dialect SQL syntax permits define a explicit limit to the number of rows returned in a query
	 */
	protected abstract boolean supportsCountLimit();

	/**
	 * 
	 * @return true if this dialect SQL syntax permits define a explicit offset for the rows returned
	 */
	protected abstract boolean supportsOffSet();


	protected String startDelimiter() {
		return startDelimiter;
	}

	protected String endDelimiter() {
		return endDelimiter;
	}

	protected String fieldSeparator() {
		return fieldSeparator;
	}

	protected void writeQueryHardname(StringBuilder buffer , QualifiedName hardname){
		buffer.append(startDelimiter);
		buffer.append(hardname.getTableName().toLowerCase());
		buffer.append(endDelimiter);
		buffer.append(fieldSeparator);
		buffer.append(startDelimiter);
		buffer.append(hardname.getColumnName().toLowerCase());
		buffer.append(endDelimiter);
	}

	protected void writeEditionHardname(StringBuilder buffer , QualifiedName hardname){
		buffer.append(startDelimiter);
		buffer.append(hardname.getTableName().toLowerCase());
		buffer.append(endDelimiter);
		if (!hardname.getColumnName().isEmpty()){
		buffer.append(fieldSeparator);
		buffer.append(startDelimiter);
		buffer.append(hardname.getColumnName().toLowerCase());
		buffer.append(endDelimiter);
		}
	}

	protected void writeEnclosureHardname(StringBuilder buffer , String hardname){
		buffer.append(startDelimiter);
		buffer.append(hardname);
		buffer.append(endDelimiter);
	}
	
	public StorageException handleSQLException(SQLException e) {
		return new StorageException(e.getMessage());
	}

	public final <T> RetriveDataBaseCommand createSelectCommand (Criteria<T> criteria, StorableEntityModel model ){

		return newCriteriaInterpreter(merge(criteria,model),model).translateRetrive();
	}


	public  DataBaseCommand createInsertCommand(Collection<Storable> data,StorableEntityModel model){
		StringBuilder names = new StringBuilder();
		StringBuilder values = new StringBuilder();
		List<StorableFieldModel> fields = new ArrayList<StorableFieldModel>();

		this.writeEditionHardname(names, model.identityFieldModel().getHardName());
		names.append(",");
		values.append("?,");
		fields.add(model.identityFieldModel());


		for ( StorableFieldModel fm : model.fields()){
			if (fm.isKey()){
				continue;
			}
			this.writeEditionHardname(names, fm.getHardName());
			names.append(",");
			values.append("?,");
			fields.add(fm);
		}

		names.delete(names.length()-1, names.length());
		values.delete(values.length()-1, values.length());

		StringBuilder sql = new StringBuilder("INSERT INTO ")
		.append(model.getEntityHardName())
		.append(" (")
		.append(names)
		.append(") VALUES (")
		.append(values)
		.append(")");

		return new SQLStoreCollectionCommand(data,sql.toString(),fields);
	}
	
	protected <T> Criteria<T> merge(Criteria<T> criteria, StorableEntityModel model){
		if (criteria instanceof DBCriteria){
			return (DBCriteria)criteria;
		}
		DBCriteria<T> merged = new DBCriteria<T>((AbstractCriteria)criteria);
		merged.restrictAll(model);
		return merged;
		
	}
	
	private class DBCriteria<T> extends AbstractCriteria<T>{

		public DBCriteria(AbstractCriteria<T> other) {
			super(other);
		}
		
		public void restrictAll(StorableEntityModel model){
			
			this.setRestrictions(restrictLogic(restrictions(),model));
		}
		
		private LogicCriterion restrictLogic(LogicCriterion l , StorableEntityModel model){
			
			LogicCriterion n = new LogicCriterion(l.getOperator());
			for (Criterion c : l){
				n.add(restrict(c,model));
			}
			
			return n;
		}
		
		private Criterion restrict(Criterion c , StorableEntityModel model){
			
			if (c instanceof FieldCriterion){
				FieldCriterion fc = (FieldCriterion)c;
				fc.valueHolder().setDataType(model.fieldModel(fc.getFieldName()).getDataType());
				return fc;
			}
			
			return c;
		}
	}
	
	public <T> DataBaseCommand createDeleteCommand(Criteria<T> criteria, StorableEntityModel model){
		
		return newCriteriaInterpreter(merge(criteria,model), model).translateDelete();

	}

	
	public DataBaseCommand createUpdateCommand(Collection<Storable> data,StorableEntityModel model){
		StringBuilder sql = new StringBuilder("UPDATE ")
		.append(model.getEntityHardName())
		.append(" SET ");

		List<StorableFieldModel> fields = new ArrayList<StorableFieldModel>();

		for ( StorableFieldModel fm : model.fields()){
			this.writeEditionHardname(sql, fm.getHardName());
			sql.append("=? ,");
			fields.add(fm);
		}

		sql.delete(sql.length()-1, sql.length());
		sql.append(" WHERE ");

		this.writeEditionHardname(sql, model.identityFieldModel().getHardName());

		sql.append("=?");
		fields.add(model.identityFieldModel());
		
		return new SQLStoreCollectionCommand(data,sql.toString(),fields);
	}

	public CriteriaInterpreter newCriteriaInterpreter(Criteria<?> criteria,StorableEntityModel model) {
		return new CriteriaInterpreter(this, criteria, model);

	}

	public abstract Sequence<Long> getSequence(DataSource ds, String identifiableName);


	public boolean supportsIntervalOf(DataType dataType) {
		return !dataType.isReference();
	}

	/**
	 * The dialect may add other specific tablemodels to the current model
	 * @param dbModel
	 */
	public void updateDatabaseModel(DataBaseModel dbModel) {
		//no-op
	}

	public DataBaseModel readDataBaseModel(String catalog, DataSource ds) {
		Connection con=null;
		try{
			con = ds.getConnection();
			DatabaseMetaData md = con.getMetaData();
			if (md==null){
				throw new StorageException("Metadata is not supported");
			}
			DataBaseModel dbm = new DataBaseModel();

			ResultSet tables = md.getTables(catalog , null,null,null);
			while (tables.next()) {
				if ( tables.getString(4).equals("TABLE")){
					TableModel tm = new TableModel(tables.getString(3));

					ResultSet columns = md.getColumns(catalog, null, tm.getName(), null);
					while (columns.next()) {

						DataType type = this.typeFromNative(columns.getInt(5));
						ColumnModel col = new ColumnModel(columns.getString(4), type);
						if (type.isTextual()){
							col.setSize(columns.getInt(7));
						}
						if (type.isDecimal()){
							col.setPrecision((columns.getInt(7)));
							col.setSize(columns.getInt(9));
						}

						col.setNullable(columns.getString(18).equals("YES"));
						tm.addColumn(col);
					}

					dbm.addTable(tm);
				}
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

	protected DataType typeFromNative(int sqlType) {
		switch (sqlType){
		case Types.INTEGER:
		case Types.TINYINT:	
			return DataType.INTEGER;
		case Types.BIT:
		case Types.BOOLEAN:
			return DataType.LOGIC;
		case Types.DATE:
			return DataType.DATE;
		case Types.TIME:
			return DataType.TIME;
		case Types.TIMESTAMP:
			return DataType.DATETIME;
		case Types.VARCHAR:
		case Types.CHAR:
			return DataType.TEXT;
		case Types.CLOB:
			return DataType.MEMO;
		}
		return DataType.TEXT;
	}

	public EditionDataBaseCommand createCreateTableCommand(TableModel tm){

		StringBuilder sql = new StringBuilder("CREATE TABLE ");
		writeEnclosureHardname(sql, tm.getName());
		sql.append("(\n ");
		for (ColumnModel cm : tm){
			writeEnclosureHardname(sql,cm.getName());
			sql.append(" ");
			appendNativeTypeFor(sql , cm);
			if(!cm.isNullable()){
				sql.append(" NOT ");
			} 
			sql.append(" NULL ");
			if (cm.isKey()){
				sql.append("CONSTRAINT PK_").append(cm.getName());
			} 
			sql.append(",\n");
		}
		sql.delete(sql.length()-2, sql.length());
		sql.append(")");
		return new SQLEditCommand(sql.toString());
	}


	protected abstract void appendNativeTypeFor(StringBuilder sql, ColumnModel type);


	public EditionDataBaseCommand createCreateIndexCommand(ColumnModel cm){

		StringBuilder sql = new StringBuilder("CREATE ");
		if (cm.isUnique()){
			sql.append(" UNIQUE ");
		}
		sql.append("INDEX idx_")
		.append(cm.getTableModel().getName()).append("_").append(cm.getName())
		.append(" ON ").append(cm.getTableModel().getName());

		return new SQLEditCommand(sql.toString());
	}





}
