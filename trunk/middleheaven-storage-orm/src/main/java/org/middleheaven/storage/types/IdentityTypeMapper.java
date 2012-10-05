/**
 * 
 */
package org.middleheaven.storage.types;

import org.middleheaven.persistance.DataColumn;
import org.middleheaven.persistance.DataRow;
import org.middleheaven.persistance.model.DataColumnModel;
import org.middleheaven.util.coersion.TypeCoercing;

/**
 * 
 */
public class IdentityTypeMapper implements TypeMapper {

	
	private Class<?> type;

	public IdentityTypeMapper (Class<?> type){
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
	public Object read(DataRow row, Object aggregateParent, DataColumnModel... columns) {
	
		
		final DataColumn column = row.getColumn(columns[0].getName());
		
		Object value = column.getValue();
		
		return TypeCoercing.coerce(value, type);
		
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(Object parent, Object object, DataRow row, DataColumnModel... columns) {
		
		String stringValue = object.toString();
		
		if (columns[0].getType().isInteger()){
			row.getColumn(columns[0].getName()).setValue(TypeCoercing.coerce(stringValue, Integer.class));
		} else {
			row.getColumn(columns[0].getName()).setValue(stringValue);
		}
		
		
	}

}
