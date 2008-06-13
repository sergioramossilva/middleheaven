package org.middleheaven.util.measure.time;

import org.middleheaven.util.measure.time.clocks.ClockTicked;
import org.middleheaven.work.scheduled.Schedule;

/**
 * Common abstract class for all clocks hat depend on other, reference, clock
 *
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
