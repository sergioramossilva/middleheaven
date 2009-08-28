package org.middleheaven.util.conversion;

import java.util.Date;

import org.middleheaven.quantity.time.CalendarDate;
import org.middleheaven.quantity.time.TimeUtils;

public class CalendarDateTypeConverter extends AbstractTypeConverter<CalendarDate,Date> {

	public CalendarDateTypeConverter(){}

	@Override
	public <T extends Date> T convertFoward(CalendarDate value, Class<T> type) {
		return type.cast(new Date(value.miliseconds()));
	}

	@Override
	public <T extends CalendarDate> T convertReverse(Date value, Class<T> type) {
		return type.cast(TimeUtils.from(value).toDate());
	}

	

	
}
