package org.middleheaven.quantity.money.rate;

import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.money.Currency;

public interface CurrencyRateService {

	public Real getRate(Currency from, Currency to);
	
}
