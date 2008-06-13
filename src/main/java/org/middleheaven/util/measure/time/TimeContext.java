package org.middleheaven.util.measure.time;

import org.middleheaven.util.measure.time.chono.JavaCalendarCronology;
import org.middleheaven.util.measure.time.clocks.MachineClock;
import org.middleheaven.util.measure.time.ephemeris.DefaultEphemeridModel;
import org.middleheaven.util.measure.time.ephemeris.EphemeridModel;
import org.middleheaven.util.measure.time.zones.JavaTimeZoneTable;

public final class TimeContext {

	
	private static TimeContext current = new TimeContext(
			new MachineClock(),
			new DefaultEphemeridModel(),
			new JavaCalendarCronology(),
			new JavaTimeZoneTable()
			);
	
	public static void setTimeContext(TimeContext context){
		current = context;
	}
	
	public static TimeContext getTimeContext(){
		return current;
	}
	

	private Clock referenceClock;
	private EphemeridModel referenceWorkCalendar;
	private Chronology chronology;
	private TimeZoneTable timezoneTable;
	
	public TimeContext(Clock referenceClock,
			EphemeridModel referenceWorkCalendar, 
			Chronology chronology,
			TimeZoneTable timezoneTable) {
		
		this.timezoneTable = timezoneTable;
		this.referenceClock = referenceClock;
		this.chronology = chronology;
		this.chronology.setClock(referenceClock);
		this.referenceWorkCalendar = referenceWorkCalendar;
		this.referenceWorkCalendar.setChronology(chronology);
		
	}

	public TimeZoneTable getTimeZoneTable(){
		return timezoneTable;
	}
	
	public TimeZone getDefaultTimeZone(){
		return timezoneTable.convertFromJavaTimeZone(java.util.TimeZone.getDefault());
	}
	
	public TimePoint now (){
		return new CalendarDateTime(this, referenceClock.getTime().milliseconds());
	}
	
	/**
	 * @return
	 * 
	 */
	public Clock getReferenceClock() {
		return referenceClock;
	}

	/**
	 * @param referenceClock
	 * 
	 */
	public void setReferenceClock(Clock referenceClock) {
		this.referenceClock = referenceClock;
	}

	/**
	 * @return
	 * 
	 */
	public EphemeridModel getReferenceWorkCalendar() {
		return referenceWorkCalendar;
	}

	/**
	 * @param referenceWorkCalendar
	 * 
	 */
	public void setReferenceWorkCalendar(EphemeridModel referenceWorkCalendar) {
		this.referenceWorkCalendar = referenceWorkCalendar;
	}

	/**
	 * @return
	 * 
	 */
	public Chronology getChronology() {
		return chronology;
	}

	/**
	 * @param chronology
	 * 
	 */
	public void setChronology(Chronology chronology) {
		this.chronology = chronology;
	}
	
	
}
