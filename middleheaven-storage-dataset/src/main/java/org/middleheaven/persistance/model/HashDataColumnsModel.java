/**
 * 
 */
package org.middleheaven.persistance.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.middleheaven.util.QualifiedName;

/**
 * 
 */
public class HashDataColumnsModel implements DataColumnsModel {

	
	private final Map<QualifiedName , DataColumnModel> models = new HashMap<QualifiedName , DataColumnModel>();
	
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
	public DataColumnModel getDataColumnModel(String columnName) {
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
	 * @param column
	 */
	public void addColumn(DataColumnModel column) {
		this.models.put(column.getName(), column);
	}
	
	

}
