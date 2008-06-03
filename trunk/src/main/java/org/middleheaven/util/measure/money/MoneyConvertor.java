/*
 * Created on 2007/01/06
 *
 */
package org.middleheaven.util.measure.money;

public interface MoneyConvertor {

    
    public Money convert(Money money, Currency targetCurrency); 
}
