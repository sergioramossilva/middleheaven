package org.middleheaven.storage.types;

import org.middleheaven.persistance.DataRow;
import org.middleheaven.persistance.model.DataColumnModel;
import org.middleheaven.quantity.money.Currency;

public class CurrencyTypeMapper implements TypeMapper {

	private static final CurrencyTypeMapper ME = new CurrencyTypeMapper();
	
	
	public static CurrencyTypeMapper instance(){
		return ME;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public String getMappedClassName() {
		return Currency.class.getName();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object read(DataRow row, Object aggregateParent, DataColumnModel ... columns) {
		
		String isoCode = (String) row.getColumn(columns[0].getName()).getValue();
		
		return Currency.currency(isoCode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(Object object, DataRow row, DataColumnModel ... columns) {
		Currency c  = (Currency) object;
		
		row.getColumn(columns[0].getName()).setValue(c.toString());
		
	}

}
