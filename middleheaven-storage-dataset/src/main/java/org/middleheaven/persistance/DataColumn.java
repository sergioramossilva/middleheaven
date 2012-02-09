package org.middleheaven.persistance;

import org.middleheaven.persistance.model.DataColumnModel;

/**
 * 
 */
public interface DataColumn {

	/**
	 * This column model.
	 * @return this column model.
	 */
	public DataColumnModel getModel();
	
	/**
	 * This column value for the current row.
	 * @param <T> the field type.
	 * @return the value of the field.
	 */
	public Object getValue();
	
	/**
	 * Set this column value for the current row.
	 * @param <T> Type of the value class
	 * @param <V> the value class.
	 * @param value the fiedl's value.
	 */
	public void setValue(Object  value);

	
}
