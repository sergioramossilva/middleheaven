package org.middleheaven.persistance.db.mapping;

import java.util.Collection;

import org.middleheaven.persistance.db.metamodel.DBColumnModel;
import org.middleheaven.persistance.db.metamodel.DBTableModel;
import org.middleheaven.persistance.model.DataColumnModel;
import org.middleheaven.persistance.model.DataSetModel;
import org.middleheaven.util.QualifiedName;

/**
 * Maps the {@link DataSetModel} conceptual model to the {@link DBTableModel} physical model.
 */
public interface DataBaseMapper {

	/**
	 * Maps a {@link DataSetModel} to a {@link DBTableModel}.
	 * @param modelName the {@link DataSetModel} to map to a {@link DBTableModel}. 
	 * @return the mapped {@link DBTableModel}.
	 */
	public DBTableModel getTableForDataSet(String modelName);

	/**
	 * Maps a {@link DataColumnModel} to a {@link DBColumnModel}.
	 * @param qn the logic {@link QualifiedName} of the column.
	 * @return the mapped {@link DBColumnModel}.
	 */
	DBColumnModel getColumnModel(QualifiedName qn);
	
	
	public Collection<DBTableModel> getTableModels();

}
