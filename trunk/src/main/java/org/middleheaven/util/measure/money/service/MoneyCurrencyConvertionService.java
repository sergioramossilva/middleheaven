package org.middleheaven.util.measure.money.service;

import org.middleheaven.util.measure.time.CalendarDate;

public interface MoneyCurrencyConvertionService {

	
	public MoneyConverter getConverterAt(CalendarDate date);
}
