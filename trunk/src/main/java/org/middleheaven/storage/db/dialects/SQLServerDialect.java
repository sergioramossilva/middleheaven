package org.middleheaven.storage.db.dialects;

import java.util.Collection;
import java.util.Collections;


import org.middleheaven.domain.DataType;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.StorableFieldModel;
import org.middleheaven.storage.StorageException;
import org.middleheaven.storage.criteria.FieldValueHolder;
import org.middleheaven.storage.db.ColumnModel;
import org.middleheaven.storage.db.DataBaseCommand;
import org.middleheaven.storage.db.DataBaseModel;
import org.middleheaven.storage.db.RetriveDataBaseCommand;
import org.middleheaven.storage.db.SQLRetriveCommand;
import org.middleheaven.storage.db.SQLStoreCollectionCommand;
import org.middleheaven.storage.db.SequenceNotSupportedDBDialect;
import org.middleheaven.storage.db.TableModel;

public class SQLServerDialect extends SequenceNotSupportedDBDialect{

	public SQLServerDialect() {
		super("[", "]", ".");
	}


	public void updateDatabaseModel (DataBaseModel dbModel){
		// add sequence table
		TableModel sequencesTable = new TableModel("tb_sequences");
		sequencesTable.addColumn(new ColumnModel("name",DataType.TEXT));
		sequencesTable.addColumn(new ColumnModel("lastUsed",DataType.INTEGER));

		dbModel.addTable(sequencesTable);
	}

	@Override
	protected RetriveDataBaseCommand createSequenceStateReadCommand(String sequenceName) {
		final Collection<FieldValueHolder> none = Collections.emptySet();
		return new SQLRetriveCommand(
				new StringBuilder("SELECT lastUsed FROM tb_sequences WHERE name ='")
				.append(sequenceName)
				.append("'")
				.toString(),
				none
		);
	}

	@Override
	protected DataBaseCommand createUpdateSequenceValueCommand(String name,long lastUsed) {
		Collection<StorableFieldModel> fields = Collections.emptySet();
		Collection<Storable> data = Collections.emptySet();

		StringBuilder sql = new StringBuilder("UPDATE tb_sequences SET lastUsed=")
		.append(lastUsed)
		.append(" WHERE name='")
		.append(name)
		.append("'");

		return new SQLStoreCollectionCommand(data,sql.toString(),fields);
	}

	@Override
	protected DataBaseCommand createInsertSequenceValueCommand(String name) {
		Collection<StorableFieldModel> fields = Collections.emptySet();
		Collection<Storable> data = Collections.emptySet();

		StringBuilder sql = new StringBuilder("INSERT INTO tb_sequences (name,lastUsed) VALUES ('")
		.append(name)
		.append("',0)");

		return new SQLStoreCollectionCommand(data,sql.toString(),fields);
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
			sql.append("nvarchar (").append(column.getSize()).append(")");
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
