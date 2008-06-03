package org.middleheaven.global.calendar;

import java.util.Set;

import org.middleheaven.util.measure.time.DateHolder;
import org.middleheaven.util.measure.time.Month;
import org.middleheaven.utils.time.MonthOfYear;

public abstract class CalendarModel {

	/**
	 * 
	 * @param date date for witch ephemeris may be gathered
	 * @return a Set of all ephemeris found to the given date
	 */
	public abstract Set<Ephemeris> getEphemeris(DateHolder date);
	
	/**
	 * 
	 * @param date date for witch ephemeris may exist
	 * @return <code>true</code> if there are any ephemeris for the given date, <code>false</code> otherwise.
	 */
	public abstract boolean hasEphemeris(DateHolder date);
	
	/**
	 * 
	 * @param date date to test as a working date
	 * @return <code>true</code> if <code>date</code> is a working day, <code>false</code> otherwise.
	 */
	public abstract boolean isWorkingDay(DateHolder date);
	
	
	public DateHolder getOrdinalWorkingDayOfMonth(Month month , int ordinal){
		DateHolder current = month.start();
		int remaining = ordinal-1; // convert ordinal to numeral
		
		while (remaining>0){
			if (this.isWorkingDay(current)){
				remaining--;
			}
			current = current.next();
		}

		return current;
	}
	
	public int workingDaysBetween (DateHolder start,DateHolder end){
		
		DateHolder current = start;
		int count=0;
		while (current.isBefore(end)){
			if (this.isWorkingDay(current)){
				count++;
			}
			current = current.next();
		}
		
		return count;
	}
	
	public DateHolder addWorkingDays(int daysCount, DateHolder start){
		int remaining = daysCount;
		DateHolder current = start;
		while (remaining>0){
			current = current.next();
			if (this.isWorkingDay(current)){
				remaining--;
			}
		}
		return current;
	}
	
	public DateHolder subtractWorkingDays(int daysCount,DateHolder start){
		int remaining = daysCount;
		DateHolder current = start;
		while (remaining>0){
			current = current.previous();
			if (this.isWorkingDay(current)){
				remaining--;
			}
		}
		return current;
	}
	
}
