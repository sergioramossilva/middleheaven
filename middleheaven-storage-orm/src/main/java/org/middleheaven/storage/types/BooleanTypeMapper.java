/**
 * 
 */
package org.middleheaven.storage.types;

import org.middleheaven.persistance.DataRow;
import org.middleheaven.persistance.model.DataColumnModel;

/**
 * 
 */
public class BooleanTypeMapper implements TypeMapper  {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMappedClassName() {
		return Boolean.class.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object read(DataRow row, Object aggregateParent,
			DataColumnModel... columns) {
		
		return (Boolean) row.getColumn(columns[0].getName()).getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(Object object, DataRow row, DataColumnModel... columns) {
		
		row.getColumn(columns[0].getName()).setValue((Boolean) object);
	}

}
