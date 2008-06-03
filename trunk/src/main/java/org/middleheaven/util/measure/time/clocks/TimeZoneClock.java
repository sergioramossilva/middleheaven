package org.middleheaven.util.measure.time.clocks;

import java.util.TimeZone;

import org.middleheaven.util.measure.time.Clock;
import org.middleheaven.util.measure.time.LocalTimePoint;
import org.middleheaven.util.measure.time.TimePoint;
import org.middleheaven.util.measure.time.TimeUtils;

public class TimeZoneClock implements Clock {


	TimeZone timeZone;
	Clock referenceClock;
	
	public TimeZoneClock(TimeZone timeZone){
		this.timeZone = timeZone;
		this.referenceClock = new LocalMachineClock();
	}
	
	public TimeZoneClock(TimeZone timeZone, Clock referenceClock){
		this.timeZone = timeZone;
		this.referenceClock = referenceClock;
	}
	

	public void setReferenceClock (Clock referenceClock){
		this.referenceClock = referenceClock;
	}
	
	@Override
	public TimeZone getTimeZone() {
		return timeZone;
	}

	@Override
	public TimePoint now() {
		return LocalTimePoint.fromUniversalTime(
				TimeUtils.localToUniversalTime(referenceClock.now().milliseconds(), referenceClock.getTimeZone()), 
				timeZone
				);
	}

}
