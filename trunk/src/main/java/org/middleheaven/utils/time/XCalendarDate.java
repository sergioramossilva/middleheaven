package org.middleheaven.utils.time;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class XCalendarDate implements DateHolder {

	int day;
	int month;
	int year;
	
	public XCalendarDate(int year, int month, int day) {
		super();
		this.day = day;
		this.month = month;
		this.year = year;
	}
	
	@Override
	public int day() {
		return day;
	}
	@Override
	public int month() {
		return month;
	}
	@Override
	public int year() {
		return year;
	}
	
	public boolean equals(Object other) {
		return other instanceof XCalendarDate && equals((XCalendarDate) other);
	}

	public boolean equals(XCalendarDate other) {
		return this.day== other.day && this.month == other.month && this.year == other.year; 
	}

	public int hashCode() {
		return 0;
	}

	public String toString(){
		return year + "-" + month + "-" + day;
	}

	@Override
	public DateHolder subtract(int amount) {
		Calendar c = new GregorianCalendar();
		c.set(year, month-1, day);
		c.add(Calendar.DATE, -amount);
		return new  XCalendarDate (c.get(Calendar.YEAR) , c.get(Calendar.MONTH)+1, c.get(Calendar.DATE));
	}

	@Override
	public DateHolder add(int amount) {
		Calendar c = new GregorianCalendar();
		c.set(year, month-1, day);
		c.add(Calendar.DATE, amount);
		return new  XCalendarDate (c.get(Calendar.YEAR) , c.get(Calendar.MONTH)+1, c.get(Calendar.DATE));
	
	}

	@Override
	public DateHolder reduceTo(int wednesday) {
		Calendar c = new GregorianCalendar();
		c.set(year, month-1, day);
		while (c.get(Calendar.DAY_OF_WEEK) != Calendar.WEDNESDAY){
			c.add(Calendar.DATE, -1);
		}
		
		return new  XCalendarDate (c.get(Calendar.YEAR) , c.get(Calendar.MONTH)+1, c.get(Calendar.DATE));
	
	}

	@Override
	public boolean isBefore(DateHolder other) {
		Calendar a = new GregorianCalendar();
		a.set(year, month-1, day);
		Calendar b = new GregorianCalendar();
		b.set(other.year(), other.month()-1, other.day());

		return a.compareTo(b)<0;
	}

	@Override
	public DateHolder next() {
		return add(1);
	}

	@Override
	public DateHolder previous() {
		return add(-1);
	}

	@Override
	public boolean isWeekDay(int weekDay) {
		Calendar a = new GregorianCalendar();
		a.set(year, month-1, day);
		
		return a.get(Calendar.DAY_OF_WEEK)==weekDay;
	}
	
	
}
