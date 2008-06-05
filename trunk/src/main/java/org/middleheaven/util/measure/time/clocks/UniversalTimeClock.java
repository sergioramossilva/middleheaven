package org.middleheaven.util.measure.time.clocks;


import org.middleheaven.util.measure.time.Clock;
import org.middleheaven.util.measure.time.TimeZone;

public final class UniversalTimeClock extends TimeZoneClock{

	public UniversalTimeClock(Clock referenceClock) {
		super(TimeZone.getTimeZone("GMT+0"),referenceClock);
	}


	

}
