package org.middleheaven.util.measure.time;

public abstract class EphemeridModel {


	protected Chronology chronology;
	

	public void setChronology(Chronology chronology){
		this.chronology = chronology;
	}
	
	public abstract boolean isWeekend(DateHolder dateHolder);
	public abstract boolean isHoliday(DateHolder dateHolder);
	public abstract boolean isWorkingDay(DateHolder dateHolder);
}
