/**
 * 
 */
package org.middleheaven.storage.types;

import org.middleheaven.core.reflection.inspection.Introspector;
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
	
		
		Object value = row.getColumn(columns[0].getName()).getValue();
		
		return Introspector.of(type).newInstance(value);
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(Object object, DataRow row, DataColumnModel... columns) {
		
		String stringValue = object.toString();
		
		if (columns[0].getType().isInteger()){
			row.getColumn(columns[0].getName()).setValue(TypeCoercing.coerce(stringValue, Integer.class));
		} else {
			row.getColumn(columns[0].getName()).setValue(stringValue);
		}
		
		
	}

}
