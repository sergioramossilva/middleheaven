package org.middleheaven.util.measure.money.service;

import org.middleheaven.util.measure.money.MoneyConvertor;
import org.middleheaven.util.measure.time.CalendarDate;

public interface MoneyCurrencyConvertionService {

	
	public MoneyConvertor converterFor(CalendarDate date);
}
