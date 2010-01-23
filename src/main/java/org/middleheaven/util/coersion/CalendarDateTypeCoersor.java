package org.middleheaven.util.coersion;

import java.util.Date;

import org.middleheaven.quantity.time.CalendarDate;
import org.middleheaven.quantity.time.TimeUtils;

public class CalendarDateTypeCoersor extends AbstractTypeCoersor<CalendarDate,Date> {

	public CalendarDateTypeCoersor(){}

	@Override
	public <T extends Date> T coerceForward(CalendarDate value, Class<T> type) {
		return type.cast(new Date(value.miliseconds()));
	}

	@Override
	public <T extends CalendarDate> T coerceReverse(Date value, Class<T> type) {
		return type.cast(TimeUtils.from(value).toDate());
	}

		
}
