package org.middleheaven.persistance;

import java.util.Collection;

import org.middleheaven.persistance.criteria.DataSetCriteria;

/**
 * A set of DataRows.
 */
public interface DataSet {

	/**
	 * 
	 * @return the {@link DataSetName} corresponding with this dataSet.
	 */
	public String getName();
	
	public void insert(Collection<DataRow> dataRows);
	
	public void delete(Collection<DataRow> dataRows);
	
	public void delete(DataSetCriteria criteria);
	
	public void update(Collection<DataRow> dataRows);
	
}
