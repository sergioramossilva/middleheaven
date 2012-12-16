/**
 * 
 */
package org.middleheaven.storage.dataset.mapping;


/**
 * 
 */
public interface EditableDatasetRepositoryModel extends DatasetRepositoryModel{

	
	public void addDatasetModel(EditableDatasetModel model);

	/**
	 * @param entityName
	 * @return
	 */
	public EditableDatasetModel getDataSetModel(String entityName);
}
