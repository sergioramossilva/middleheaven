package org.middleheaven.util.conversion;

import java.util.Date;

import org.middleheaven.global.text.TimepointFormatter;
import org.middleheaven.quantity.time.TimeUtils;

public class StringDateConverter extends AbstractTypeConverter<String, Date> {

	TimepointFormatter format;
	public StringDateConverter(TimepointFormatter format){
		this.format = format;
	}
	
	@Override
	public <T extends Date> T convertFoward(String value, Class<T> type) {
		try {
			if (value==null || value.trim().isEmpty()){
				return null;
			}
			
			return type.cast(format.parse(value));
		} catch (RuntimeException e) {
			throw new ConvertionException(e);
		}
		
	}

	@Override
	public <T extends String> T convertReverse(Date value, Class<T> type) {
		if (value==null){
			return null;
		}
		return type.cast(format.format(TimeUtils.from(value)));
	}



}
