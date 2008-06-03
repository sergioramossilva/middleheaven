package org.middleheaven.util.measure.time;

import java.sql.Timestamp;
import java.util.Date;
import java.util.TimeZone;

public final class TimeUtils {

	private TimeUtils(){};
	
	public static long universalToLocalTime (long universalTime , TimeZone a){
		return universalTime + a.getRawOffset() + a.getOffset(universalTime);
	}
	
	public static long localToUniversalTime (long localtime , TimeZone a){
		return localtime - a.getRawOffset() - a.getOffset(System.currentTimeMillis());
	}
	
	public static long convertLocalTime (long localTime , TimeZone a , TimeZone b){
		return localTime + b.getRawOffset() - a.getRawOffset() ;
		
	}
	
	public static Date toDate(TimePoint date){
		return new Date(date.milliseconds());
	}
	
	public static java.sql.Date toSQLDate(TimePoint date){
		return new java.sql.Date(date.milliseconds());
	}
	
	public static java.sql.Time toSQLTime(TimePoint date){
		return new java.sql.Time(date.milliseconds());
	}
	
	public static Timestamp toSQLTimestamp(TimePoint date){
		return new java.sql.Timestamp(date.milliseconds());
	}
	
	public static int compare(Date a, TimePoint b){
		return (int)(a.getTime() - b.milliseconds());
	}
}
