package org.middleheaven.storage.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.middleheaven.storage.QualifiedName;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.StorableFieldModel;
import org.middleheaven.storage.StorageException;
import org.middleheaven.storage.criteria.AbstractCriteria;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.storage.criteria.CriteriaBuilder;
import org.middleheaven.storage.criteria.Criterion;
import org.middleheaven.storage.criteria.FieldCriterion;
import org.middleheaven.storage.criteria.FieldValueHolder;
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

	public String startDelimiter() {
		return startDelimiter;
	}

	public String endDelimiter() {
		return endDelimiter;
	}

	public String fieldSeparator() {
		return fieldSeparator;
	}

	public void writeQueryHardname(StringBuilder buffer , QualifiedName hardname){
		buffer.append(startDelimiter);
		buffer.append(hardname.getTableName().toLowerCase());
		buffer.append(endDelimiter);
		buffer.append(fieldSeparator);
		buffer.append(startDelimiter);
		buffer.append(hardname.getColumnName().toLowerCase());
		buffer.append(endDelimiter);
	}

	public void writeEditionHardname(StringBuilder buffer , QualifiedName hardname){
		buffer.append(startDelimiter);
		buffer.append(hardname.getTableName().toLowerCase());
		buffer.append(endDelimiter);
		buffer.append(fieldSeparator);
		buffer.append(startDelimiter);
		buffer.append(hardname.getColumnName().toLowerCase());
		buffer.append(endDelimiter);
	}

	public abstract StorageException handleSQLException(SQLException e);

	public final <T> RetriveDataBaseCommand createSelectCommand (Criteria<T> criteria, StorableEntityModel model ){

		return newCriteriaInterpreter(merge(criteria,model),model).translateRetrive();
	}


	public  DataBaseCommand createInsertCommand(Collection<Storable> data,StorableEntityModel model){
		StringBuilder names = new StringBuilder();
		StringBuilder values = new StringBuilder();
		List<StorableFieldModel> fields = new ArrayList<StorableFieldModel>();

		this.writeEditionHardname(names, model.keyFieldModel().getHardName());
		names.append(",");
		values.append("?,");
		fields.add(model.keyFieldModel());


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
		.append(model.hardNameForEntity())
		.append(" (")
		.append(names)
		.append(") VALUES (")
		.append(values)
		.append(")");

		return new SQLStoreCollectionCommand(data,sql.toString(),fields);
	}
	
	public <T> Criteria<T> merge(Criteria<T> criteria, StorableEntityModel model){
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
		.append(model.hardNameForEntity())
		.append(" SET ");

		List<StorableFieldModel> fields = new ArrayList<StorableFieldModel>();

		for ( StorableFieldModel fm : model.fields()){
			this.writeEditionHardname(sql, fm.getHardName());
			sql.append("=? ,");
			fields.add(fm);
		}

		sql.delete(sql.length()-1, sql.length());
		sql.append(" WHERE ");

		this.writeEditionHardname(sql, model.keyFieldModel().getHardName());

		sql.append("=?");
		fields.add(model.keyFieldModel());
		
		return new SQLStoreCollectionCommand(data,sql.toString(),fields);
	}

	public CriteriaInterpreter newCriteriaInterpreter(Criteria<?> criteria,StorableEntityModel model) {
		return new CriteriaInterpreter(this, criteria, model);

	}


}
