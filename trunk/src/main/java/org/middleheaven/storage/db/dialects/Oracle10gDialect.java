package org.middleheaven.storage.db.dialects;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.reflection.PropertyNotFoundException;
import org.middleheaven.domain.DataType;
import org.middleheaven.storage.QualifiedName;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.StorableFieldModel;
import org.middleheaven.storage.StorageException;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.storage.criteria.CriterionOperator;
import org.middleheaven.storage.criteria.FieldValueHolder;
import org.middleheaven.storage.db.ColumnModel;
import org.middleheaven.storage.db.CriteriaInterpreter;
import org.middleheaven.storage.db.DataBaseModel;
import org.middleheaven.storage.db.DataBaseObjectAlreadyExistsException;
import org.middleheaven.storage.db.DataBaseObjectModel;
import org.middleheaven.storage.db.DataBaseObjectType;
import org.middleheaven.storage.db.EditionDataBaseCommand;
import org.middleheaven.storage.db.RetriveDataBaseCommand;
import org.middleheaven.storage.db.SQLEditCommand;
import org.middleheaven.storage.db.SQLRetriveCommand;
import org.middleheaven.storage.db.SequenceModel;
import org.middleheaven.storage.db.SequenceSupportedDBDialect;
import org.middleheaven.storage.db.TableModel;

public class Oracle10gDialect extends SequenceSupportedDBDialect{

	public Oracle10gDialect() {
		super("\"", "\"", ".");
	}

	public CriteriaInterpreter newCriteriaInterpreter(Criteria<?> criteria,
			StorableEntityModel model) {
		return new OracleCriteriaInterpreter(criteria, model);

	}

	public void writeEditionHardname(StringBuilder buffer , QualifiedName hardname){

			buffer.append(startDelimiter());
			buffer.append(hardname.getName().toLowerCase());
			buffer.append(endDelimiter());
		
	}
	
	public List<String> resolveCatalog(DataSource ds) {
		try {
			// the catalog is in fact the user schema.
			// the user must be returned
			ds = unwrapp(ds);
			
			// faster
			PropertyAccessor pa = Introspector.of(ds.getClass()).inspect().properties().named("login").retrive();

			return Collections.singletonList(pa.getValue(ds).toString().toUpperCase());
		} catch (PropertyNotFoundException e){
			
			return Collections.emptyList();
		}

	}
	
	protected static DataSource unwrapp(DataSource ds){
		try{
			if (ds.isWrapperFor(DataSource.class)){
				return unwrapp(ds.unwrap(DataSource.class));
			} else {
				return ds;
			}
		} catch (SQLException e) {
			throw new StorageException(e);
		}
	}
	
	public StorageException handleSQLException(SQLException e) {
		String msg = e.getMessage();
		int code = e.getErrorCode();
		
		if (code==955){
			throw new DataBaseObjectAlreadyExistsException();
		}
		if (e.getNextException()!=null){
			msg += "\n" + e.getNextException().getMessage();
		}
		return new StorageException("[" + this.getClass().getSimpleName()+ "]" + msg);
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

			ResultSet tables = md.getTables( null ,catalog ,null,null);
			try{
				while (tables.next()) {
					if ("TABLE".equals(tables.getString(4))){
						TableModel tm = new TableModel(tables.getString(3));

						ResultSet columns = md.getColumns(null, catalog, tm.getName(), null);
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
						dbm.addDataBaseObjectModel(new SequenceModel(tables.getString(3),1,1));
					}
				}
			} finally{
				tables.close();
			}




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

	public  EditionDataBaseCommand createCreateTableCommand(TableModel tm){

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
				sql.append("PRIMARY KEY ");
			} 
			sql.append(",\n");
		}
		sql.delete(sql.length()-2, sql.length());
		sql.append(")");
		return new SQLEditCommand(this,sql.toString());
	}

	public void updateDatabaseModel(DataBaseModel model){

		List<SequenceModel> sequences = new LinkedList<SequenceModel>();

		for (DataBaseObjectModel table : model){
			if(table.getType().equals(DataBaseObjectType.TABLE)){
				sequences.add(new SequenceModel("seq_" + table.getName() , 0,1));
			}
		}

		for (SequenceModel seq : sequences){
			model.addDataBaseObjectModel(seq);
		}
	}


	private  class OracleCriteriaInterpreter extends CriteriaInterpreter{

		public OracleCriteriaInterpreter(Criteria<?> criteria, StorableEntityModel model) {
			super(Oracle10gDialect.this, criteria, model);
		}

		protected void writeAliasSeparator(StringBuilder queryBuffer){
			queryBuffer.append(" ");
		}
		
		protected void writeAlias(StringBuilder queryBuffer , String alias){
			queryBuffer.append(startDelimiter())
			.append(alias)
			.append(endDelimiter());
		}	

		
		protected void writeEndLimitClause(StringBuilder selectBuffer){
			if (criteria().getCount()>0 && criteria().getStart()>1){
				
				if (selectBuffer.charAt(selectBuffer.length()-1)==')'){
					selectBuffer.append(" AND ");
				}
				selectBuffer.append("rownum between ")
				.append(criteria().getStart())
				.append(" and ")
				.append(criteria().getStart() + criteria().getCount()-1);
			}
		}

		protected void writeLikeClause(StorableFieldModel fm,StringBuilder criteriaBuffer,
				boolean caseSensitive, CriterionOperator op,String alias) {
		
				if (caseSensitive){
					writeQueryHardname(criteriaBuffer, aliasFor(fm.getHardName(),alias));

					if (op.isNegated()){
						criteriaBuffer.append(" NOT");
					}
					
					criteriaBuffer.append(" LIKE ? "); // case sensitive
				} else {
					
					if (op.isNegated()){
						criteriaBuffer.append(" NOT");
					}
					
				
					writeQueryHardname(criteriaBuffer, aliasFor(fm.getHardName(),alias));
					criteriaBuffer.append(" LIKE ?");
							
				}
				
			}
		
	}

	@Override
	protected <T> RetriveDataBaseCommand createNextSequenceValueCommand(String sequenceName) {
		return new SQLRetriveCommand(this,
				new StringBuilder("SELECT ")
				.append("seq_").append(sequenceName) //avoid name colision
				.append(".nextval FROM dual")
				.toString() ,
				Collections.<FieldValueHolder>emptySet()
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

	@Override
	protected void appendNativeTypeFor(StringBuilder sql, ColumnModel column) {
		switch (column.getType()){ 
		case DATE:
			sql.append("timestamp");
			break;
		case DATETIME:
			sql.append("timestamp");
			break;
		case TIME:
			sql.append("timestamp");
			break;
		case TEXT:
			sql.append("nvarchar2 (").append(column.getSize()).append(")");
			break;
		case INTEGER:
			sql.append("number");
			break;
		case LOGIC:
		case ENUM:
			sql.append("number (1)");
			break;
		case DECIMAL:
			sql.append("number (").append(column.getPrecision()).append(",").append(column.getSize()).append(")");
			break;
		default:
			throw new StorageException(column.getType() + " is not convertible to a native column type");
		}
	}

	@Override
	public EditionDataBaseCommand createCreateSequenceCommand(SequenceModel sequence) {

		StringBuilder sql = new StringBuilder("CREATE SEQUENCE ")
		.append(sequence.getName()) // avoid name colision
		.append(" INCREMENT BY ").append(sequence.getIncrementBy())
		.append(" MINVALUE ").append(sequence.getStartWith())
		.append(" START WITH " ).append(sequence.getStartWith());


		return new SQLEditCommand(this,sql.toString());
	}
}
