package org.middleheaven.util.measure.time;

import java.text.DecimalFormat;



public abstract class TimeZone {


	public static TimeZone getDefault() {
		return TimeContext.getTimeContext().getDefaultTimeZone();
	}
	
	public static TimeZone getTimeZone(String reference) {
		return TimeContext.getTimeContext().getTimeZoneTable().getTimeZone(reference);
	}
	
	public final Period getRawOffsetPeriod(){
		return  new Period(getRawOffset());
	}
	
	public abstract long getRawOffset();

	public abstract long getOffset(long universalTime);

	public abstract java.util.TimeZone toTimeZone();



}
