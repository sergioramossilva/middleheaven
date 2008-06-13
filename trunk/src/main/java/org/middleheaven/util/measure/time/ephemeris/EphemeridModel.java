package org.middleheaven.util.measure.time.ephemeris;

import java.io.Serializable;
import java.util.Set;

import org.middleheaven.util.measure.time.Chronology;
import org.middleheaven.util.measure.time.DateHolder;
import org.middleheaven.util.measure.time.DayOfWeek;
import org.middleheaven.util.measure.time.Month;


public abstract class EphemeridModel implements Serializable{


	private Chronology chronology;
	

	public final void setChronology(Chronology chronology){
		this.chronology = chronology;
	}
	
	protected final Chronology getChronology(){
		return this.chronology;
	}
	
	public boolean isWeekend(DateHolder dateHolder){
	    final DayOfWeek dw =  dateHolder.dayOfWeek();
	    return dw.equals(DayOfWeek.SATURDAY) || dw.equals(DayOfWeek.SUNDAY);
	}
	

	public boolean isWorkingDay(DateHolder dateHolder){
		return !isWeekend(dateHolder) && !isHoliday(dateHolder);
	}

	public boolean isHoliday(DateHolder dateHolder) {
		
		final Set<Ephemeris> ephemerides = getEphemeris(dateHolder);
		for (Ephemeris e : ephemerides){
			if (e.isHoliday()){
				return true;
			}
		}
		return false;
		
	}
	
	public boolean hasEphemeris(DateHolder date){
		return !getEphemeris(date).isEmpty();
	}

	public abstract Set<Ephemeris> getEphemeris(DateHolder date);

	public final DateHolder getOrdinalWorkingDayOfMonth(Month month , int ordinal){
		DateHolder current = month.start();
		int remaining = ordinal-1; // convert ordinal to numeral
		
		while (remaining>0){
			if (this.isWorkingDay(current)){
				remaining--;
			}
			current = current.nextDate();
		}

		return current;
	}
	
	public final int workingDaysBetween (DateHolder start,DateHolder end){
		
		DateHolder current = start;
		int count=0;
		while (current.isBefore(end)){
			if (this.isWorkingDay(current)){
				count++;
			}
			current = current.nextDate();
		}
		
		return count;
	}
	
	public final DateHolder addWorkingDays(int daysCount, DateHolder start){
		int remaining = daysCount;
		DateHolder current = start;
		while (remaining>0){
			current = current.nextDate();
			if (this.isWorkingDay(current)){
				remaining--;
			}
		}
		return current;
	}
	
	public final DateHolder subtractWorkingDays(int daysCount,DateHolder start){
		int remaining = daysCount;
		DateHolder current = start;
		while (remaining>0){
			current = current.previousDate();
			if (this.isWorkingDay(current)){
				remaining--;
			}
		}
		return current;
	}
	
}
