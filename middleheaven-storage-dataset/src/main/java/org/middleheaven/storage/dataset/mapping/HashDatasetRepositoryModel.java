/**
 * 
 */
package org.middleheaven.storage.dataset.mapping;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.collections.enumerable.Enumerables;

/**
 * 
 */
public class HashDatasetRepositoryModel implements EditableDatasetRepositoryModel {

	final Map<String, EditableDatasetModel> models = new HashMap<String, EditableDatasetModel>();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<DatasetModel> models() {
		return Enumerables.asEnumerable(models.values()).cast(DatasetModel.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addDatasetModel(EditableDatasetModel model) {
		models.put(model.getName().toLowerCase(), model);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EditableDatasetModel getDataSetModel(String entityName) {
		return models.get(entityName.toLowerCase());
	}

}
