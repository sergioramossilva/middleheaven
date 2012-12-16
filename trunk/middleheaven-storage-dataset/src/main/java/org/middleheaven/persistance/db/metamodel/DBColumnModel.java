package org.middleheaven.persistance.db.metamodel;

import org.middleheaven.persistance.model.ColumnValueType;
import org.middleheaven.util.QualifiedName;

/**
 * The model for a row column.
 */
public interface DBColumnModel {

	public String getLogicName();

	public DBColumnModel duplicate();

	public void setTableModel(DBTableModel beanTableModel);


	public boolean isNullable();


	public boolean isKey();
	
	public boolean isVersion();


	public boolean isUnique();

	public String getUniqueGroupName();

	public int getSize();

	public int getPrecision();


	public ColumnValueType getType();

	public DBTableModel getTableModel();

	public QualifiedName getName();

	/**
	 * @return
	 */
	public boolean isIndexed();

}
