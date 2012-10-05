/**
 * 
 */
package org.middleheaven.storage.types;

import org.middleheaven.persistance.DataRow;
import org.middleheaven.persistance.model.DataColumnModel;

/**
 * 
 */
public class NumberTypeMapper implements TypeMapper  {
	
	
	private Class<? extends Number> type;

	public NumberTypeMapper (Class<? extends Number> type){
		this.type = type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMappedClassName() {
		return type.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object read(DataRow row, Object aggregateParent,
			DataColumnModel... columns) {
		
		return type.cast(row.getColumn(columns[0].getName()).getValue());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(Object parent, Object object, DataRow row, DataColumnModel... columns) {
		
		row.getColumn(columns[0].getName()).setValue(object);
	}

}
