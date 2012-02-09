/**
 * 
 */
package org.middleheaven.persistance;

import org.middleheaven.persistance.model.DataSetModel;

/**
 * 
 */
public interface DataSchemaModel {

	/**
	 * 
	 * @return <code>true</code> is the DataStore permits the model edition.
	 */
	public boolean isEditable();

	/**
	 * @param name
	 * @return
	 */
	public DataSetModel getDataSetModel(DataSetName name);
}
