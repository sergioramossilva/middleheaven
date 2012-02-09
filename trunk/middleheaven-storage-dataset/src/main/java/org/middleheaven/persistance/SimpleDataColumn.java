/**
 * 
 */
package org.middleheaven.persistance;

import org.middleheaven.persistance.model.DataColumnModel;

/**
 * 
 */
public class SimpleDataColumn implements DataColumn {

	
	private DataColumnModel model;
	private Object value;

	public SimpleDataColumn (DataColumnModel model){
		this.model = model;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataColumnModel getModel() {
		return model;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getValue() {
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(Object value) {
		this.value = value;
	}

}
