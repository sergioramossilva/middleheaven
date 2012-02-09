/**
 * 
 */
package org.middleheaven.persistance.db;

import org.middleheaven.persistance.db.metamodel.DBColumnModel;

/**
 * 
 */
public class ParameterizedValueHolder implements ValueHolder {

	private String paramName;
	private DBColumnModel model;
	
	public ParameterizedValueHolder (String paramName , DBColumnModel model){
		this.paramName = paramName;
		this.model = model;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getValue(QueryParameters parameters) {
		return parameters.getValue(paramName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DBColumnModel getModel() {
		return model;
	}

}
