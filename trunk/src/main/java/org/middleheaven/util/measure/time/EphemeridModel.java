package org.middleheaven.util.measure.time;

import java.util.Set;

import org.middleheaven.global.calendar.Ephemeris;

public abstract class EphemeridModel {


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

	public abstract Set<Ephemeris> getEphemeris(DateHolder date);
	
}
