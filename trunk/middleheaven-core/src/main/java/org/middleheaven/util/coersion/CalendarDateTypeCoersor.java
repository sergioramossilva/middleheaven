package org.middleheaven.util.coersion;

import java.util.Date;

import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.quantity.time.CalendarDate;
import org.middleheaven.quantity.time.CalendarDateTime;
import org.middleheaven.quantity.time.TimeUtils;

public class CalendarDateTypeCoersor<CD extends CalendarDateTime, D extends Date> extends AbstractTypeCoersor<CD,D> {

	public static <F extends CalendarDateTime,E extends Date>  CalendarDateTypeCoersor<F,E> getInstance(Class<F> calendarDateTime, Class<E> date){
		return new CalendarDateTypeCoersor<F,E>(calendarDateTime,date);
	}

	private Class<CD> calendarDateTimeType;
	private Class<D> dateType;
	
	private CalendarDateTypeCoersor(Class<CD> calendarDateTimeType, Class<D> dateType){
		this.calendarDateTimeType = calendarDateTimeType;
		this.dateType = dateType;
	}

	@Override
	public <T extends D> T coerceForward(CD value, Class<T> type) {
		return type.cast(Introspector.of(dateType).newInstance(Long.valueOf(value.getMilliseconds())));
	}

	@Override
	public <T extends CD> T coerceReverse(D value, Class<T> type) {
		CalendarDateTime cdt = TimeUtils.from(value);
		if (type.isAssignableFrom(CalendarDate.class)){
			return type.cast(cdt.toDate());
		}
		return type.cast(cdt);
	}

		
}
