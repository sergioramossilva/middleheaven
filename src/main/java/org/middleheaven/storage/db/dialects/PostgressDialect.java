package org.middleheaven.storage.db.dialects;

import java.sql.SQLException;
import java.util.Collections;

import org.middleheaven.storage.QualifiedName;
import org.middleheaven.storage.StorableFieldModel;
import org.middleheaven.storage.StorableModelReader;
import org.middleheaven.storage.StorageException;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.storage.criteria.CriterionOperator;
import org.middleheaven.storage.db.Clause;
import org.middleheaven.storage.db.ColumnModel;
import org.middleheaven.storage.db.ColumnValueHolder;
import org.middleheaven.storage.db.CriteriaInterpreter;
import org.middleheaven.storage.db.DataBaseDialect;
import org.middleheaven.storage.db.EditionDataBaseCommand;
import org.middleheaven.storage.db.RetriveDataBaseCommand;
import org.middleheaven.storage.db.SQLEditCommand;
import org.middleheaven.storage.db.SQLRetriveCommand;
import org.middleheaven.storage.db.SequenceModel;
import org.middleheaven.storage.db.SequenceSupportedDBDialect;

public class PostgressDialect extends SequenceSupportedDBDialect{

	public PostgressDialect() {
		super("\"", "\"", ".");
	}

	@Override
	public CriteriaInterpreter newCriteriaInterpreter(Criteria<?> criteria,
			StorableModelReader reader) {
		return new PostgressCriteriaInterpreter(this, criteria, reader);
	}
	
	protected void appendInlineCreateTableColumnPrimaryKeyConstraint(Clause sql, String constraintName){
		sql.append(" CONSTRAINT PK_").append(constraintName).append(" PRIMARY KEY ");
	}

	@Override
	protected <T> RetriveDataBaseCommand createNextSequenceValueCommand(String sequenceName) {
		return new SQLRetriveCommand(this,
				new StringBuilder("SELECT nextval('")
				.append("pgseq_").append(sequenceName) //avoid name colision
				.append("') as sequenceValue")
				.toString() ,
				Collections.<ColumnValueHolder>emptySet()
		);
	}
	
	@Override
	public EditionDataBaseCommand createCreateSequenceCommand(SequenceModel sequence) {
		
		StringBuilder sql = new StringBuilder("CREATE SEQUENCE ")
		.append("pgseq_").append(sequence.getName()) // avoid name colision
		.append(" INCREMENT BY ").append(sequence.getIncrementBy())
		.append(" MINVALUE ").append(sequence.getStartWith())
		.append(" START WITH " ).append(sequence.getStartWith());
		

		return new SQLEditCommand(this,sql.toString());
	}
	
	@Override
	public StorageException handleSQLException(SQLException e) {
		String msg = e.getMessage();
		if (e.getNextException()!=null){
			msg += "\n" + e.getNextException().getMessage();
		}
		return new StorageException(msg);
	}

	public void writeQueryHardname(StringBuilder buffer , QualifiedName hardname){


		buffer.append(hardname.getQualifier().toLowerCase());
		buffer.append(fieldSeparator());
		buffer.append(hardname.getName().toLowerCase());
	}
	
	public void writeEditionHardname(StringBuilder buffer , QualifiedName hardname){

		buffer.append(hardname.getName().toLowerCase());

	}
	
	protected void writeEnclosureHardname(StringBuilder buffer , String hardname){
		buffer.append(startDelimiter());
		buffer.append(hardname.toLowerCase());
		buffer.append(endDelimiter());
	}

	private class PostgressCriteriaInterpreter extends CriteriaInterpreter{

		public PostgressCriteriaInterpreter(DataBaseDialect dataBaseDialect,
				Criteria<?> criteria, StorableModelReader reader) {
			super(dataBaseDialect, criteria, reader);
		}

		protected void writeFromClause(StringBuilder queryBuffer){

			// FROM ClAUSE
			queryBuffer.append(" FROM ");
			queryBuffer.append(model().getEntityHardName().toLowerCase());

		}
		
		protected void writeEndLimitClause(StringBuilder selectBuffer){
			if (criteria().getCount()>0){
				selectBuffer.append(" LIMIT ").append(criteria().getCount());
				if (criteria().getStart()>1){
					selectBuffer.append(" OFFSET ").append(criteria().getStart()-1);
				}
			}
		}
		
		protected void writeLikeClause(StorableFieldModel fm,Clause criteriaBuffer,
				boolean caseSensitive, CriterionOperator op,String alias) {
			dialect().writeQueryHardname(criteriaBuffer, dialect().aliasFor(fm.getHardName(),alias));

			if (op.isNegated()){
				criteriaBuffer.append(" NOT");
			}
			if (caseSensitive){
				criteriaBuffer.append(" LIKE ? "); // case sensitive
			} else {
				criteriaBuffer.append(" ILIKE ? "); // case insensitive
			}
		}
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
	protected void appendNativeTypeFor(Clause sql, ColumnModel column) {
		switch (column.getType()){ // TODO verificar na documentação postgress
		case DATE:
			sql.append("date");
			break;
		case DATETIME:
			sql.append("timestamp");
			break;
		case TIME:
			sql.append("time");
			break;
		case TEXT:
			sql.append("varchar (").append(column.getSize()).append(")");
			break;
		case MEMO:
			sql.append("text");
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


}
