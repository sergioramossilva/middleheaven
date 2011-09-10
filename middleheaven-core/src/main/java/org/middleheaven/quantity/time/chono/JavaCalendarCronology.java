package org.middleheaven.quantity.time.chono;

import java.util.Calendar;
import java.util.Locale;

import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.quantity.time.AbstractChronology;
import org.middleheaven.quantity.time.DayOfMonth;
import org.middleheaven.quantity.time.DayOfWeek;
import org.middleheaven.quantity.time.Duration;
import org.middleheaven.quantity.time.Month;
import org.middleheaven.quantity.time.TimeHolder;
import org.middleheaven.quantity.time.TimePoint;
import org.middleheaven.quantity.time.Year;


public class JavaCalendarCronology extends AbstractChronology{


	private Calendar prototype;

	public JavaCalendarCronology(){
		this(Calendar.getInstance(Locale.getDefault()));
	}

	public JavaCalendarCronology(Calendar prototype){
		this.prototype = prototype;
	}

	public JavaCalendarCronology(Locale locale){
		this(Calendar.getInstance(locale));
	}

	
	@Override
	public <T extends TimePoint> T add(T point, Duration duration) {
		Calendar calendar = (Calendar)prototype.clone();
		calendar.setLenient(true);
		calendar.setTimeInMillis(point.getMilliseconds());
		calendar.add(Calendar.YEAR, duration.years());
		calendar.add(Calendar.MONTH, duration.months());
		calendar.add(Calendar.DATE, duration.days());
		calendar.add(Calendar.HOUR, duration.hours());
		calendar.add(Calendar.MINUTE, duration.minutes());
		calendar.add(Calendar.MILLISECOND, (int)(duration.secounds()*1000));
	
		return reduce(
				timePointFor(calendar.getTimeInMillis()), 
				Introspector.of(point).getType() 
		);
	}



	@Override
	public long milisecondsFor(boolean lenient, int year, int month, int day) {
		if (year <=0){
			throw new IllegalArgumentException("Year must be a positive integer");
		}
		if (day <=0){
			throw new IllegalArgumentException("Day must be a positive integer");
		}
		if (month <=0){
			throw new IllegalArgumentException("Month must be a positive integer");
		}

		Calendar calendar = (Calendar)prototype.clone();
		clearCalendar(calendar);
		calendar.setLenient(lenient);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month-1);
		calendar.set(Calendar.DATE, day);
		calendar.clear(Calendar.HOUR_OF_DAY);
		calendar.clear(Calendar.MINUTE);
		calendar.clear(Calendar.SECOND);
		calendar.clear(Calendar.MILLISECOND);
		
		return calendar.getTimeInMillis();
	}

	protected void clearCalendar(Calendar calendar){
		for (int i=0;i<Calendar.FIELD_COUNT;i++){
			calendar.clear(i);
		}
	}
	
	@Override
	public DayOfMonth monthDay(TimePoint point) {
		Calendar c = (Calendar)prototype.clone();
		c.setTimeInMillis(point.getMilliseconds());
		return new DayOfMonth(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH)+1);
	}

	@Override
	public Month monthOf(TimePoint point) {
		Calendar c = (Calendar)prototype.clone();
		c.setTimeInMillis(point.getMilliseconds());
		return month( c.get(Calendar.YEAR),
				c.get(Calendar.MONTH)+1,
				c.getActualMaximum(Calendar.DAY_OF_MONTH));
	}

	@Override
	public DayOfWeek weekDay(TimePoint point) {
		Calendar c = (Calendar)prototype.clone();
		c.setTimeInMillis(point.getMilliseconds());
		return weekDay(c.get(Calendar.DAY_OF_WEEK));
	}

	@Override
	public Year yearOf(TimePoint point) {
		Calendar c =  (Calendar)prototype.clone();
		c.setTimeInMillis(point.getMilliseconds());
		return year(
				c.get(Calendar.YEAR),
				c.getActualMaximum(Calendar.MONTH)
		);
	}

	@Override
	public int yearDay(TimePoint point) {
		Calendar c =  (Calendar)prototype.clone();
		c.setTimeInMillis(point.getMilliseconds());
		return c.get(Calendar.DAY_OF_YEAR);
	}
	
	@Override
	public Month monthOf(int year, int month) {
		Calendar calendar = (Calendar)prototype.clone();
		clearCalendar(calendar);
		calendar.setLenient(false);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month-1);
		calendar.set(Calendar.DATE, 1);
		calendar.clear(Calendar.HOUR_OF_DAY);
		calendar.clear(Calendar.MINUTE);
		calendar.clear(Calendar.SECOND);
		calendar.clear(Calendar.MILLISECOND);
		
		return new Month(year, month, calendar.getGreatestMinimum(Calendar.DAY_OF_MONTH));
	}

	@Override
	public TimeHolder timeOf(TimePoint point) {
		Calendar c =  (Calendar)prototype.clone();
		c.setTimeInMillis(point.getMilliseconds());
		return new CalendarTimeHolder(c);
	}

	
	private static final class CalendarTimeHolder implements TimeHolder{
		Calendar calendar;
		public CalendarTimeHolder(Calendar calendar) {
			this.calendar = calendar;
		}

		@Override
		public int hour() {
			return calendar.get(Calendar.HOUR_OF_DAY);
		}

		@Override
		public long getMilliseconds() {
			return calendar.get(Calendar.MILLISECOND);
		}

		@Override
		public int minute() {
			return calendar.get(Calendar.MINUTE);
		}

		@Override
		public int second() {
			return calendar.get(Calendar.SECOND);
		}
		
	}



}
