/**
 * 
 */
package org.middleheaven.persistance.model;

import java.util.Collection;

/**
 * 
 */
public class EditableDataSet implements DataSetModel {

	private String dataSetName;
	private HashDataColumnsModel columns = new HashDataColumnsModel();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return this.dataSetName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataColumnsModel getModelColumns() {
		return columns;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataColumnsModel getPrimaryKeyColumns() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<DataColumnsModel> getUniqueGroupsColumns() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<DataColumnsModel> getIndexGroupsColumns() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<DataColumnsModel> getForeignKeyColumns() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * @param column
	 */
	public void addColumn(DataColumnModel column) {
		columns.addColumn(column);
	}

	/**
	 * @param dataSetName
	 */
	public void setName(String dataSetName) {
		this.dataSetName = dataSetName;
	}

}
