package org.middleheaven.util.measure.money;

import java.util.Locale;

import org.middleheaven.util.measure.CompositeUnit;
import org.middleheaven.util.measure.Dimension;
import org.middleheaven.util.measure.IncompatibleUnitsException;
import org.middleheaven.util.measure.Unit;
import org.middleheaven.util.measure.measures.Measurable;

public abstract class Currency extends Unit<org.middleheaven.util.measure.measures.Currency>{

	public static Currency currency(java.util.Currency currency ){
		return new ISOCurrency(currency);
	}
	
	public static Currency currency(String isoCode ){
		try {
			return new ISOCurrency(java.util.Currency.getInstance(isoCode));
		} catch (IllegalArgumentException e){
			throw new IllegalArgumentException(isoCode + " is not a valid ISO currency code");
		}
		
	}
	
	public static Currency currency(Locale locale ){
		return new ISOCurrency(java.util.Currency.getInstance(locale));
	}


	public abstract int getDefaultFractionDigits();
	
	@Override
	public Dimension<org.middleheaven.util.measure.measures.Currency> dimension() {
		return Dimension.CURRENCY;
	}
	
	@Override
	public Unit<org.middleheaven.util.measure.measures.Currency> minus(Unit<org.middleheaven.util.measure.measures.Currency> other) throws IncompatibleUnitsException {
		return plus(other);
	}
	
	@Override
	public Unit<org.middleheaven.util.measure.measures.Currency> plus(Unit<org.middleheaven.util.measure.measures.Currency> other) throws IncompatibleUnitsException {
		if (this.isCompatible(other)){
			return this;
		}
		throw new IncompatibleUnitsException(this,other);
	}
	
	public boolean equals (Object other){
		return other instanceof Currency && equals((Currency)other);
	}
	
	@Override
	public boolean equals(Unit<org.middleheaven.util.measure.measures.Currency> other) {
		return other.dimension().equals(Dimension.CURRENCY) && equals((Currency)other);
	}
	
	public boolean equals(Currency other) {
		return other.symbol().equals(this.symbol());
	}
	
	@Override
	public String toString() {
		return this.symbol();
	}
	
	@Override
	public boolean isCompatible(Unit<?> other) {
		return this.equals(other);
	}
	
	@Override
	public <C extends Measurable> Unit<C> cast() {
		return (Unit<C>) this;
	}

	@Override
	public <T extends Measurable> Unit<T> over(Unit<?> other) {
		return CompositeUnit.over(this,other);
	}

	@Override
	public <T extends Measurable> Unit<T> times(Unit<?> other) {
		return CompositeUnit.times(this,other);
	}

	@Override
	public Unit<org.middleheaven.util.measure.measures.Currency> raise(int value) {
		return CompositeUnit.raise (this, value);
	}

}
