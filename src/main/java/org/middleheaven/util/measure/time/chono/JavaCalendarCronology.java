package org.middleheaven.util.measure.time.chono;

import java.util.Calendar;
import java.util.Locale;

import org.middleheaven.util.measure.time.AbstractChronology;
import org.middleheaven.util.measure.time.DayOfMonth;
import org.middleheaven.util.measure.time.DayOfWeek;
import org.middleheaven.util.measure.time.Duration;
import org.middleheaven.util.measure.time.Month;
import org.middleheaven.util.measure.time.TimePoint;
import org.middleheaven.util.measure.time.Year;


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
		calendar.setTimeInMillis(point.milliseconds());
		calendar.add(Calendar.YEAR, duration.years());
		calendar.add(Calendar.MONTH, duration.months());
		calendar.add(Calendar.DATE, duration.days());
		return reduce(timePointFor(calendar.getTimeInMillis()), point.getClass());
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
		c.setTimeInMillis(point.milliseconds());
		return new DayOfMonth(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH)+1);
	}

	@Override
	public Month monthOf(TimePoint point) {
		Calendar c = (Calendar)prototype.clone();
		c.setTimeInMillis(point.milliseconds());
		return month( c.get(Calendar.YEAR),
				c.get(Calendar.MONTH)+1,
				c.getActualMaximum(Calendar.DAY_OF_MONTH));
	}

	@Override
	public DayOfWeek weekDay(TimePoint point) {
		Calendar c = (Calendar)prototype.clone();
		c.setTimeInMillis(point.milliseconds());
		return weekDay(c.get(Calendar.DAY_OF_WEEK));
	}

	@Override
	public Year yearOf(TimePoint point) {
		Calendar c =  (Calendar)prototype.clone();
		c.setTimeInMillis(point.milliseconds());
		return year(
				c.get(Calendar.YEAR),
				c.getActualMaximum(Calendar.MONTH)
		);
	}

}
