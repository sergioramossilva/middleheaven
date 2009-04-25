package org.middleheaven.util.conversion;

import java.lang.reflect.Method;
import java.util.Date;

import org.middleheaven.global.text.TimepointFormatter;
import org.middleheaven.quantity.time.CalendarDateTime;

public class StringCalendarDateTimeConverter extends AbstractTypeConverter<String, CalendarDateTime>{

	TimepointFormatter format;
	public StringCalendarDateTimeConverter(TimepointFormatter format){
		this.format = format;
	}
	
	@Override
	public <T extends CalendarDateTime> T convertFoward(String value, Class<T> type) {

		try {
			if (value==null || value.trim().isEmpty()){
				return null;
			}
			
			Method method = type.getMethod("date", new Class[]{Date.class});
			
			return type.cast( method.invoke(null, format.parse(value)));
		} catch (Exception e) {
			throw new ConvertionException(e);
		} 
	}

	@Override
	public <T extends String> T convertReverse(CalendarDateTime value, Class<T> type) {
		if (value==null){
			return null;
		}
		return type.cast(format.format(value));
	}

}
