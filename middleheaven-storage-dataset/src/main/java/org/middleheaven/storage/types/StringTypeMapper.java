/**
 * 
 */
package org.middleheaven.storage.types;

import org.middleheaven.persistance.DataRow;
import org.middleheaven.persistance.model.DataColumnModel;

/**
 * 
 */
public class StringTypeMapper implements TypeMapper  {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMappedClassName() {
		return String.class.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object read(DataRow row, Object aggregateParent,
			DataColumnModel... columns) {
		
		return (String) row.getColumn(columns[0].getName()).getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(Object parent, Object object, DataRow row, DataColumnModel... columns) {
		
		row.getColumn(columns[0].getName()).setValue((String) object);
	}

}
