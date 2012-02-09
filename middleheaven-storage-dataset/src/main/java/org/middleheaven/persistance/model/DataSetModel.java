package org.middleheaven.persistance.model;

import java.util.Collection;


public interface DataSetModel {

	
	public String getName();
	

	/**
	 * 
	 * @return all column models.
	 */
	public DataColumnsModel getModelColumns();
	
	public DataColumnsModel getPrimaryKeyColumns();
	
	public Collection<DataColumnsModel> getUniqueGroupsColumns();
	
	public Collection<DataColumnsModel> getIndexGroupsColumns();

	public Collection<DataColumnsModel> getForeignKeyColumns();



}
