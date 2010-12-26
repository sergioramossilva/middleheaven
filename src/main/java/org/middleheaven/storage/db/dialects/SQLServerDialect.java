package org.middleheaven.storage.db.dialects;

import java.util.Collection;
import java.util.Collections;

import org.middleheaven.model.domain.DataType;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.StorableFieldModel;
import org.middleheaven.storage.StorageException;
import org.middleheaven.storage.db.Clause;
import org.middleheaven.storage.db.ColumnModel;
import org.middleheaven.storage.db.ColumnValueHolder;
import org.middleheaven.storage.db.DataBaseCommand;
import org.middleheaven.storage.db.DataBaseModel;
import org.middleheaven.storage.db.RetriveDataBaseCommand;
import org.middleheaven.storage.db.SQLRetriveCommand;
import org.middleheaven.storage.db.SQLStoreCollectionCommand;
import org.middleheaven.storage.db.SequenceNotSupportedDBDialect;
import org.middleheaven.storage.db.TableBasedSequence;
import org.middleheaven.storage.db.TableModel;

public class SQLServerDialect extends SequenceNotSupportedDBDialect{

	
	private final static String SEQUENCES_TABLE_NAME = "tb_sequences";
	
	public SQLServerDialect() {
		super("[", "]", ".");
	}


	public void updateDatabaseModel (DataBaseModel dbModel){
		// add sequences control table
		TableModel sequencesTable = new TableModel(SEQUENCES_TABLE_NAME);
		sequencesTable.addColumn(new ColumnModel("name",DataType.TEXT));
		sequencesTable.addColumn(new ColumnModel("lastUsed",DataType.INTEGER));

		dbModel.addDataBaseObjectModel(sequencesTable);
	}

	@Override
	protected RetriveDataBaseCommand createSequenceStateReadCommand(TableBasedSequence sequence) {
		return new SQLRetriveCommand(this,
				new StringBuilder("SELECT lastUsed FROM ").append(SEQUENCES_TABLE_NAME).append(" WHERE name ='")
				.append(hardSequenceName(sequence.getName()))
				.append("'")
				.toString(),
				Collections.<ColumnValueHolder>emptySet()
		);
	}

	@Override
	protected DataBaseCommand createUpdateSequenceValueCommand(TableBasedSequence sequence,long lastUsed) {
		Collection<StorableFieldModel> fields = Collections.emptySet();
		Collection<Storable> data = Collections.emptySet();

		StringBuilder sql = new StringBuilder("UPDATE ").append(SEQUENCES_TABLE_NAME).append(" SET lastUsed=")
		.append(lastUsed)
		.append(" WHERE name='")
		.append(hardSequenceName(sequence.getName()))
		.append("'");

		return new SQLStoreCollectionCommand(this,data,sql.toString(),fields);
	}

	@Override
	protected DataBaseCommand createInsertSequenceValueCommand(TableBasedSequence sequence) {
		Collection<StorableFieldModel> fields = Collections.emptySet();
		Collection<Storable> data = Collections.emptySet();

		StringBuilder sql = new StringBuilder("INSERT INTO ").append(SEQUENCES_TABLE_NAME).append(" (name,lastUsed) VALUES ('")
		.append(hardSequenceName(sequence.getName()))
		.append("',0)");

		return new SQLStoreCollectionCommand(this,data,sql.toString(),fields);
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
			throw new StorageException(column.getType() + " is not convertible to a native column type");
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






}
