package org.middleheaven.util.measure.time;

import org.middleheaven.util.measure.time.chono.JavaCalendarCronology;
import org.middleheaven.util.measure.time.clocks.LocalMachineClock;

public final class TimeContext {

	
	private static TimeContext current = new TimeContext(
			new LocalMachineClock(),
			new DefaultEphemeridModel(),
			new JavaCalendarCronology()
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
	
	
	public TimeContext(Clock referenceClock,
			EphemeridModel referenceWorkCalendar, Chronology chronology) {
		
		this.referenceClock = referenceClock;
		this.chronology = chronology;
		this.chronology.setClock(referenceClock);
		this.referenceWorkCalendar = referenceWorkCalendar;
		this.referenceWorkCalendar.setChronology(chronology);
		
	}

	public TimePoint now (){
		return new CalendarDateTime(this, referenceClock.now().milliseconds());
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
