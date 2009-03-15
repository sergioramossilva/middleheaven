package org.middleheaven.global.text;

import java.util.Date;


public class TimestampFormatter implements Formatter<Object> {

	public enum Format {
		
		DATE_ONLY,
		TIME_ONLY,
		DATE_AND_TIME
	}

	private Format format = Format.DATE_AND_TIME;
	
	@Override
	public String format(Object object) {
		// TODO implement Formatter<Date>.format
		return null;
	}

	@Override
	public Object parse(String stringValue) {
		return parseDate(stringValue);
	}

	private Date parseDate(String stringValue) {
		// TODO implement Formatter<Date>.parse
		return null;
	}

	public void setPattern(Format format) {
		this.format = format;
	}

}
