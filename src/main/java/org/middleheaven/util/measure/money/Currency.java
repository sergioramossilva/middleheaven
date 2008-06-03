package org.middleheaven.util.measure.money;

import java.util.Locale;

import org.middleheaven.util.measure.Unit;

public abstract class Currency extends Unit{

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
}
