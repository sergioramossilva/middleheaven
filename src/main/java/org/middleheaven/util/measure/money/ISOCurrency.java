package org.middleheaven.util.measure.money;

import org.middleheaven.util.measure.CompositeUnit;
import org.middleheaven.util.measure.Dimension;
import org.middleheaven.util.measure.IncompatibleUnitsException;
import org.middleheaven.util.measure.Unit;



public class ISOCurrency extends Currency{


	private java.util.Currency iso;
	
	public ISOCurrency(java.util.Currency currency) {
		this.iso = currency;
	}
	
	
	public int getDefaultFractionDigits(){
		return iso.getDefaultFractionDigits();
	}
	
	public String getISOCode(){
		return iso.getCurrencyCode();
	}
	
	@Override
	public Dimension dimension() {
		return Dimension.CURRENCY;
	}
	
	@Override
	public Unit over(Unit other) {
		return CompositeUnit.over(this,other);
	}
	
	@Override
	public Unit times(Unit other) {
		return CompositeUnit.times(this,other);
	}
	
	
	@Override
	public boolean equals(Unit other) {
		return other.dimension().equals(Dimension.CURRENCY) && equals((Currency)other);
	}
	
	public boolean equals(Currency other) {
		return other.symbol().equals(this.symbol());
	}
	
	@Override
	public boolean isCompatible(Unit other) {
		return this.equals(other);
	}
	
	@Override
	public Unit minus(Unit other) throws IncompatibleUnitsException {
		return plus(other);
	}
	
	@Override
	public Unit plus(Unit other) throws IncompatibleUnitsException {
		if (this.isCompatible(other)){
			return this;
		}
		throw new IncompatibleUnitsException(this,other);
	}
	
	@Override
	public String symbol() {
		return iso.getCurrencyCode();
	}
	

	@Override
	public String toString() {
		return iso.getCurrencyCode();
	}


	@Override
	public Unit raise(int value) {
		return CompositeUnit.raise (this, value);
	}





}
