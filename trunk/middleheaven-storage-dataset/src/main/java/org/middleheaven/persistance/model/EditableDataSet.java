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
	private HashDataColumnsModel keys = new HashDataColumnsModel();
	
	public String toString () {
		return dataSetName;
	}
	
	public int hashCode(){
		return dataSetName.hashCode();
	}
	
	public boolean equals(Object other){
		return (other instanceof DataSetModel) && equalsOther((DataSetModel) other);
	}
	
	/**
	 * @param other
	 * @return
	 */
	private boolean equalsOther(DataSetModel other) {
		return this.getName().equals(other.getName());
	}

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
		return keys;
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
		
		if (column.isInPrimaryKeyGroup()){
			this.keys.addColumn(column);
		}
	}

	/**
	 * @param dataSetName
	 */
	public void setName(String dataSetName) {
		this.dataSetName = dataSetName;
	}

}
