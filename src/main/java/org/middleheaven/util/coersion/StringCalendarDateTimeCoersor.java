package org.middleheaven.util.coersion;

import java.lang.reflect.Method;
import java.util.Date;

import org.middleheaven.global.text.TimepointFormatter;
import org.middleheaven.quantity.time.CalendarDateTime;

public class StringCalendarDateTimeCoersor extends AbstractTypeCoersor<String, CalendarDateTime>{

	TimepointFormatter format;
	public StringCalendarDateTimeCoersor(TimepointFormatter format){
		this.format = format;
	}
	
	@Override
	public <T extends CalendarDateTime> T coerceForward(String value, Class<T> type) {

		try {
			if (value==null || value.trim().isEmpty()){
				return null;
			}
			
			Method method = type.getMethod("date", new Class[]{Date.class});
			
			return type.cast( method.invoke(null, format.parse(value)));
		} catch (Exception e) {
			throw new CoersionException(e);
		} 
	}

	@Override
	public <T extends String> T coerceReverse(CalendarDateTime value, Class<T> type) {
		if (value==null){
			return null;
		}
		return type.cast(format.format(value));
	}

	

}
