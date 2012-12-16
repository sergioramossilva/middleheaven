/**
 * 
 */
package org.middleheaven.storage.dataset.mapping;

import org.middleheaven.util.collections.Enumerable;


/**
 * 
 */
public interface DatasetRepositoryModel {

	public Enumerable<DatasetModel> models();
	
	/**
	 * @param entityName
	 * @return
	 */
	public DatasetModel getDataSetModel(String entityName);
}
