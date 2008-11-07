package org.middleheaven.util.measure.time;

import org.middleheaven.util.measure.time.chono.JavaCalendarCronology;
import org.middleheaven.util.measure.time.clocks.MachineClock;
import org.middleheaven.util.measure.time.ephemeris.DefaultEphemerisModel;
import org.middleheaven.util.measure.time.ephemeris.EphemerisModel;
import org.middleheaven.util.measure.time.zones.JavaTimeZoneTable;

public final class TimeContext {

	
	private static TimeContext current = new TimeContext(
			new MachineClock(),
			new DefaultEphemerisModel(),
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
	private EphemerisModel referenceWorkCalendar;
	private Chronology chronology;
	private TimeZoneTable timezoneTable;
	
	public TimeContext(Clock referenceClock,
			EphemerisModel referenceWorkCalendar, 
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
	public EphemerisModel getReferenceWorkCalendar() {
		return referenceWorkCalendar;
	}

	/**
	 * @param referenceWorkCalendar
	 * 
	 */
	public void setReferenceWorkCalendar(EphemerisModel referenceWorkCalendar) {
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
