/*
 * Created on 2007/01/06
 *
 */
package org.middleheaven.util.measure.money.service;

import org.middleheaven.util.measure.money.Currency;
import org.middleheaven.util.measure.money.Money;

public interface MoneyConverter {

    
    public Money convert(Money money, Currency targetCurrency); 
}
