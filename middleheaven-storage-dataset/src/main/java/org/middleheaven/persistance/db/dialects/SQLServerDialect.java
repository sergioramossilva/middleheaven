package org.middleheaven.persistance.db.dialects;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import org.middleheaven.persistance.DataRow;
import org.middleheaven.persistance.PersistanceException;
import org.middleheaven.persistance.db.Clause;
import org.middleheaven.persistance.db.DataBaseCommand;
import org.middleheaven.persistance.db.RetriveDataBaseCommand;
import org.middleheaven.persistance.db.SQLRetriveCommand;
import org.middleheaven.persistance.db.SQLStoreCollectionCommand;
import org.middleheaven.persistance.db.ValueHolder;
import org.middleheaven.persistance.db.metamodel.DBColumnModel;
import org.middleheaven.persistance.db.metamodel.EditableColumnModel;
import org.middleheaven.persistance.db.metamodel.EditableDBTableModel;
import org.middleheaven.persistance.db.metamodel.EditableDataBaseModel;
import org.middleheaven.persistance.db.metamodel.TableBasedSequence;
import org.middleheaven.persistance.model.ColumnValueType;

/**
 * Dialect for Microsoft SQL Server.
 */
public class SQLServerDialect extends SequenceNotSupportedDBDialect{

	
	private final static String SEQUENCES_TABLE_NAME = "tb_sequences";
	
	public SQLServerDialect() {
		super("[", "]", ".");
	}


	public void extendsDatabaseModel (EditableDataBaseModel dbModel){
		// add sequences control table
		EditableDBTableModel sequencesTable = new EditableDBTableModel(SEQUENCES_TABLE_NAME);
		sequencesTable.addColumn(new EditableColumnModel("name",ColumnValueType.TEXT));
		sequencesTable.addColumn(new EditableColumnModel("lastUsed",ColumnValueType.INTEGER));

		dbModel.addDataBaseObjectModel(sequencesTable);
	}

	@Override
	protected RetriveDataBaseCommand createSequenceStateReadCommand(TableBasedSequence sequence) {
		return new SQLRetriveCommand(this,
				new StringBuilder("SELECT lastUsed FROM ").append(SEQUENCES_TABLE_NAME).append(" WHERE name ='")
				.append(hardSequenceName(sequence.getName()))
				.append("'")
				.toString(),
				new LinkedList<ValueHolder>()
		);
	}

	@Override
	protected DataBaseCommand createUpdateSequenceValueCommand(TableBasedSequence sequence,long lastUsed) {
	
		Collection<DataRow> data = Collections.emptySet();

		StringBuilder sql = new StringBuilder("UPDATE ").append(SEQUENCES_TABLE_NAME).append(" SET lastUsed=")
		.append(lastUsed)
		.append(" WHERE name='")
		.append(hardSequenceName(sequence.getName()))
		.append("'");

	
		return new SQLStoreCollectionCommand(this,data,sql.toString());
	}

	@Override
	protected DataBaseCommand createInsertSequenceValueCommand(TableBasedSequence sequence) {
		Collection<DataRow> data = Collections.emptySet();

		StringBuilder sql = new StringBuilder("INSERT INTO ").append(SEQUENCES_TABLE_NAME).append(" (name,lastUsed) VALUES ('")
		.append(hardSequenceName(sequence.getName()))
		.append("',0)");

		return new SQLStoreCollectionCommand(this,data,sql.toString());
	}

	@Override
	protected void appendNativeTypeFor(Clause sql, DBColumnModel column) {
		switch (column.getType()){
		case DATE:
		case DATETIME:
		case TIME:
			sql.append("datetime");
			break;
		case TEXT:
			sql.append("nvarchar (").append(column.getSize()).append(")");
			break;
		case MEMO:
			sql.append("ntext");
			break;
		case INTEGER:
		case LOGIC:
			sql.append("int");
			break;
		case DECIMAL:
			sql.append("numeric (").append(column.getPrecision()).append(",").append(column.getSize()).append(")");
			break;
		default:
			throw new PersistanceException(column.getType() + " is not convertible to a native column type");
		}

	}


	@Override
	public boolean supportsCountLimit() {
		return true;
	}


	@Override
	public boolean supportsOffSet() {
		return false;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public RetriveDataBaseCommand createExistsDatabaseCommand(String name) {
		throw new UnsupportedOperationException("Not implememented yet");
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public RetriveDataBaseCommand createExistsSchemaCommand(String schemaName) {
		throw new UnsupportedOperationException("Not implememented yet");
	}






}
