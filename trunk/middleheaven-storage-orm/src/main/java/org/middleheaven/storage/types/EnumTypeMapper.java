/**
 * 
 */
package org.middleheaven.storage.types;

import org.middleheaven.persistance.DataRow;
import org.middleheaven.persistance.model.DataColumnModel;
import org.middleheaven.storage.StorableEnum;
import org.middleheaven.storage.StorableEnumUtils;

/**
 * 
 */
public class EnumTypeMapper implements TypeMapper {

	
	
	private Class<? extends StorableEnum> enumType;

	public EnumTypeMapper(Class<? extends StorableEnum> enumType){
		this.enumType = enumType;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMappedClassName() {
		return enumType.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object read(DataRow row, Object aggregateParent, DataColumnModel... columns) {
		
		Object value = row.getColumn(columns[0].getName()).getValue();
		
		return StorableEnumUtils.valueForId(enumType, value);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(Object object, DataRow row, DataColumnModel... columns) {
			
		row.getColumn(columns[0].getName()).setValue( ((StorableEnum) object).getStorableValue());

	}

}
