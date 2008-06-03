package org.middleheaven.util.measure.time.clocks;

import java.util.TimeZone;

import org.middleheaven.util.measure.time.Clock;

public abstract class UniversalTimeClock implements Clock{

	@Override
	public TimeZone getTimeZone() {
		return TimeZone.getTimeZone("GMT+0");
	}

	

}
