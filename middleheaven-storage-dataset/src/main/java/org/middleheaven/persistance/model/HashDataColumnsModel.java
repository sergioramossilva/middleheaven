/**
 * 
 */
package org.middleheaven.persistance.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.middleheaven.util.QualifiedName;

/**
 * Simple implementation of {@link DataColumnsModel} based on the qualified column's name hash.
 */
public class HashDataColumnsModel implements DataColumnsModel {

	
	private final Map<QualifiedName , DataColumnModel> models = new HashMap<QualifiedName , DataColumnModel>();
	
	/**
	 * 
	 * Constructor.
	 */
	public HashDataColumnsModel(){}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<DataColumnModel> iterator() {
		return models.values().iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataColumnModel getDataColumnModel(QualifiedName columnName) {
		return models.get(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return this.models.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return this.models.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsColumn(QualifiedName name) {
		return models.containsKey(name);
	}

	/**
	 * Add a {@link DataColumnModel} to this model.
	 * @param column the {@link DataColumnModel} to add.
	 */
	public void addColumn(DataColumnModel column) {
		this.models.put(column.getName(), column);
	}
	
	

}
