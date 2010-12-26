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

import org.middleheaven.model.domain.DataType;
import org.middleheaven.sequence.Sequence;
import org.middleheaven.storage.QualifiedName;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.StorableFieldModel;
import org.middleheaven.storage.StorableModelReader;
import org.middleheaven.storage.StorageException;
import org.middleheaven.util.criteria.Criterion;
import org.middleheaven.util.criteria.FieldCriterion;
import org.middleheaven.util.criteria.LogicCriterion;
import org.middleheaven.util.criteria.entity.AbstractEntityCriteria;
import org.middleheaven.util.criteria.entity.EntityCriteria;
import org.middleheaven.util.criteria.entity.JunctionCriterion;

/**
 * Holds informations for a specific DBMS providers.
 */
public abstract class DataBaseDialect implements AliasResolver{

	private final String startDelimiter;
	private final String endDelimiter;
	private final String fieldSeparator;
	private final AliasResolver aliasResolver = new DefaultAliasResolver(); 
	

	/**
	 * 
	 * @param startDelimiter delimiter for the start of the table or column name
	 * @param endDelimiter delimiter for the start of the table or column name
	 * @param fieldSeparator separator between the table and column names.
	 */
	protected DataBaseDialect(String startDelimiter, String endDelimiter,String fieldSeparator) {

		this.startDelimiter = startDelimiter;
		this.endDelimiter = endDelimiter;
		this.fieldSeparator = fieldSeparator;
	}
	
	public String aliasFor(String name, boolean increment){
		return aliasResolver.aliasFor(name, increment);
	}

	public  QualifiedName aliasFor(QualifiedName name, String aliasPrefix){
		return aliasResolver.aliasFor(name, aliasPrefix);
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

	/**
	 * 
	 * @return <code>true</code> if this dialect supports Batch sotrage, <code>false</code> otherwise.
	 */
	protected  boolean supportsBatch(){
		return true;
	}

	protected String startDelimiter() {
		return startDelimiter;
	}

	protected String endDelimiter() {
		return endDelimiter;
	}

	protected String fieldSeparator() {
		return fieldSeparator;
	}

	public void writeQueryHardname(Clause buffer , QualifiedName hardname){
		buffer.append(startDelimiter);
		buffer.append(hardname.getQualifier().toLowerCase());
		buffer.append(endDelimiter);
		buffer.append(fieldSeparator);
		buffer.append(startDelimiter);
		buffer.append(hardname.getName().toLowerCase());
		buffer.append(endDelimiter);
	}

	protected void writeEditionHardname(Clause buffer , QualifiedName hardname){
		buffer.append(startDelimiter);
		buffer.append(hardname.getQualifier().toLowerCase());
		buffer.append(endDelimiter);
		if (!hardname.getName().isEmpty()){
			buffer.append(fieldSeparator);
			buffer.append(startDelimiter);
			buffer.append(hardname.getName().toLowerCase());
			buffer.append(endDelimiter);
		}
	}

	protected void writeEnclosureHardname(Clause buffer , String hardname){
		buffer.append(startDelimiter);
		buffer.append(hardname);
		buffer.append(endDelimiter);
	}

	public StorageException handleSQLException(SQLException e) {
		return new StorageException(e.getMessage());
	}

	public final <T> RetriveDataBaseCommand createSelectCommand (EntityCriteria<T> criteria, StorableModelReader reader){

		return newCriteriaInterpreter(mergeCriteria(criteria,reader.read(criteria.getTargetClass())),reader).translateRetrive();
	}

	public EditionDataBaseCommand createCreateSequenceCommand(SequenceModel dbtype) {

		throw new UnsupportedOperationException(this.getClass().getName() + " does not support native sequence objects");
	}

	/**
	 * Only creates, does not remove
	 * @param tm table model
	 * @return
	 */
	public EditionDataBaseCommand createAlterTableCommand(TableModel tm) {

		Clause sql = new Clause("ALTER TABLE ");
		writeEnclosureHardname(sql, tm.getName());
		sql.append("\n");
		for (ColumnModel cm : tm) {
			sql.append("ADD ");
			writeEnclosureHardname(sql,cm.getName());
			sql.append(" ");
			appendNativeTypeFor(sql , cm);
			sql.append(",\n");
		}

		sql.removeLastCharacters(2);

		return new SQLEditCommand(this,sql.toString());
	}

	/**
	 * Creates insert command.
	 * @param data collection of storables to be inserted.
	 * @param model entity store model.
	 * @return the insert command.
	 */
	public  DataBaseCommand createInsertCommand(Collection<Storable> data, StorableEntityModel model) {
		Clause names = new Clause();
		Clause values = new Clause();
		List<StorableFieldModel> fields = new ArrayList<StorableFieldModel>();

		this.writeEditionHardname(names, model.identityFieldModel().getHardName());
		names.append(",");
		values.append("?,");
		fields.add(model.identityFieldModel());


		for (StorableFieldModel fm : model.fields()) {
			if (fm.isIdentity() || fm.getDataType().isToManyReference()){
				continue;
			}
			this.writeEditionHardname(names, fm.getHardName());
			names.append(",");
			values.append("?,");
			fields.add(fm);
		}

		names.removeLastChar();
		values.removeLastChar();

		Clause sql = new Clause("INSERT INTO ");
		this.writeEnclosureHardname(sql, model.getEntityHardName());
		
		sql.append(" (")
		.append(names)
		.append(") VALUES (")
		.append(values)
		.append(")");
		
	

		return new SQLStoreCollectionCommand(this,data,sql.toString(),fields);
	}

	protected <T> EntityCriteria<T> mergeCriteria(EntityCriteria<T> criteria, StorableEntityModel model){
		if (criteria instanceof DBCriteria){
			final DBCriteria<T> dbCriteria = (DBCriteria<T>)criteria;
			return dbCriteria;
		}
		DBCriteria<T> merged = new DBCriteria<T>((AbstractEntityCriteria<T>)criteria);
		merged.restrictAll(model);
		return merged;

	}

	private class DBCriteria<T> extends AbstractEntityCriteria<T>{

		public DBCriteria(AbstractEntityCriteria<T> other) {
			super(other);
		}

		public void restrictAll(StorableEntityModel model){

			this.setRestrictions(restrictLogic(constraints(),model));
		}

		private LogicCriterion restrictLogic(LogicCriterion l , StorableEntityModel model){

			LogicCriterion n = new LogicCriterion(l.getOperator());
			for (Criterion c : l){
				n.add(restrict(c,model));
			}

			return n;
		}

		private Criterion restrict(Criterion c , StorableEntityModel model){

			if (c instanceof JunctionCriterion) {
				
			} else if (c instanceof FieldCriterion){
				FieldCriterion fc = (FieldCriterion)c;
				DataType dataType = model.fieldModel(fc.getFieldName()).getDataType();
				fc.valueHolder().setDataType(dataType);
				return fc;
			}

			return c;
		}

		@Override
		public EntityCriteria<T> duplicate() {
			return new DBCriteria<T>(this);
		}
	}

	public <T> DataBaseCommand createDeleteCommand(EntityCriteria<T> criteria, StorableModelReader reader ){

		return newCriteriaInterpreter(mergeCriteria(criteria,reader.read(criteria.getTargetClass())), reader).translateDelete();

	}


	public DataBaseCommand createUpdateCommand(Collection<Storable> data,StorableEntityModel model){
		Clause sql = new Clause("UPDATE ");

		this.writeEnclosureHardname(sql, model.getEntityHardName());

		sql.append(" SET ");

		List<StorableFieldModel> fields = new ArrayList<StorableFieldModel>();

		for (StorableFieldModel fm : model.fields()) {
			if (!fm.isTransient() && !fm.getDataType().isToManyReference()) {
				this.writeEditionHardname(sql, fm.getHardName());
				sql.append("=? ,");
				fields.add(fm);
			}
		}

		sql.removeLastChar().append(" WHERE ");

		this.writeEditionHardname(sql, model.identityFieldModel().getHardName());

		sql.append("=?");
		fields.add(model.identityFieldModel());

		return new SQLStoreCollectionCommand(this,data,sql.toString(),fields);
	}

	public CriteriaInterpreter newCriteriaInterpreter(EntityCriteria<?> criteria,StorableModelReader reader) {
		return new CriteriaInterpreter(this, criteria, reader);

	}

	public abstract Sequence<Long> getSequence(DataSource ds, String identifiableName);


	protected boolean supportsIntervalOf(DataType dataType) {
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
				if ("TABLE".equals(tables.getString(4))){
					TableModel tm = new TableModel(tables.getString(3));

					ResultSet columns = md.getColumns(catalog, null, tm.getName(), null);
					try{
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

							col.setNullable("YES".equals(columns.getString(18)));
							tm.addColumn(col);
						}

					} finally {
						columns.close();
					}
					dbm.addDataBaseObjectModel(tm);
				} else if ("SEQUENCE".equals(tables.getString(4))){
					dbm.addDataBaseObjectModel(new SequenceModel(logicSequenceName(tables.getString(3)),1,1));
				}
			}

			tables.close();

			return dbm;
		} catch (SQLException e) {
			throw this.handleSQLException(e);
		} finally {
			if(con != null){
				try {
					con.close();
				} catch (SQLException e) {
					throw this.handleSQLException(e);
				}
			}
		}
	}
	
	protected String hardSequenceName(String logicName){
		return "seq_".concat(logicName.toLowerCase());
	}
	
	protected String logicSequenceName(String hardName){
		return hardName.substring(4);
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
		case Types.SMALLINT:
			return DataType.ENUM;
		case Types.CLOB:
			return DataType.MEMO;
		}
		return DataType.TEXT;
	}

	public  EditionDataBaseCommand createCreateTableCommand(TableModel tm){

		Clause sql = new Clause("CREATE TABLE ");
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
				appendInlineCreateTableColumnPrimaryKeyConstraint(sql,cm);
			} 
			sql.append(",\n");
		}
		sql.removeLastCharacters(2);
		sql.append(")");
		return new SQLEditCommand(this,sql.toString());
	}

	
	protected  void appendInlineCreateTableColumnPrimaryKeyConstraint(Clause sql, ColumnModel column){
		sql.append(" CONSTRAINT PK_").append(column.getTableModel().getName()).append("_").append(column.getName());
	}
	
	protected abstract void appendNativeTypeFor(Clause sql, ColumnModel type);


	public EditionDataBaseCommand createCreateIndexCommand(ColumnModel cm){

		StringBuilder sql = new StringBuilder("CREATE ");
		if (cm.isUnique()){
			sql.append(" UNIQUE ");
		}
		sql.append("INDEX idx_")
		.append(cm.getTableModel().getName()).append("_").append(cm.getName())
		.append(" ON ").append(cm.getTableModel().getName());

		return new SQLEditCommand(this,sql.toString());
	}

	public void writeJoinTableHardname(Clause joinClause, String hardNameForEntity) {
		joinClause.append(startDelimiter())
		.append(hardNameForEntity)
		.append(endDelimiter());
	}


	public void writeJoinField(Clause joinClause, String alias ,String fieldName) {
		joinClause.append(startDelimiter())
		.append(alias)
		.append(endDelimiter())
		.append(fieldSeparator())
		.append(startDelimiter())
		.append(fieldName)
		.append(endDelimiter());
	}





}
