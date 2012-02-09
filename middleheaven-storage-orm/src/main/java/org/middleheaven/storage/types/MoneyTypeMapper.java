package org.middleheaven.storage.types;

import org.middleheaven.persistance.DataRow;
import org.middleheaven.persistance.model.DataColumnModel;
import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.money.Currency;
import org.middleheaven.quantity.money.Money;

public class MoneyTypeMapper implements TypeMapper {

	
	public MoneyTypeMapper(){}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMappedClassName() {
		return Money.class.getName();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object read(DataRow row, Object aggregateParent, DataColumnModel ... columns) {
		
		
		Real amount = (Real) RealTypeMapper.instance().read(row, aggregateParent, columns[0]);
		
		Currency c = (Currency) CurrencyTypeMapper.instance().read(row, aggregateParent, columns[1]);
		
		return Money.money(amount, c);
		
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(Object object, DataRow row, DataColumnModel ... columns) {
	
		Money money = (Money) object;
		
		RealTypeMapper.instance().write(money.amount(), row, columns[0]);
		
		CurrencyTypeMapper.instance().write(money.unit(), row, columns[1]);
	
		
	}



}
