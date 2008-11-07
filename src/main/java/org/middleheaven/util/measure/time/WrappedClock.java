package org.middleheaven.util.measure.time;

import org.middleheaven.util.measure.time.clocks.ClockTicked;
import org.middleheaven.work.scheduled.Schedule;

/**
 * Encapsulates another Clock implementation in order to change the original clock behaviour
 * or drive the new Clock properties from the encapsulated clock.  
 *
 */
public abstract class WrappedClock extends Clock {

	private Clock referenceClock;
	
	public WrappedClock(Clock referenceClock){
		this.referenceClock = referenceClock;
	}
	
	protected Clock getReferenceClock(){
		return referenceClock;
	}
	
	protected void setReferenceClock (Clock referenceClock){
		this.referenceClock = referenceClock;
	}
	
	@Override
	public TimePoint getTime() {
		return referenceClock.getTime();
	}
	
	@Override
	public double getCadence() {
		return referenceClock.getCadence();
	}
	
	@Override
	public TimeZone getTimeZone() {
		return referenceClock.getTimeZone();
	}
	
	@Override
	protected ClockTicked schedule(Schedule schedule, Clock clock) {
		return referenceClock.schedule(schedule, this);
	}
	
	protected TimePoint calculateTimeFromReference(TimePoint referenceTime){
		return referenceTime;
	}
}
