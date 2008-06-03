package org.middleheaven.util.measure.time.clocks;

import java.util.TimeZone;

import org.middleheaven.util.measure.time.Clock;
import org.middleheaven.util.measure.time.LocalTimePoint;
import org.middleheaven.util.measure.time.TimePoint;

public class LocalMachineClock implements Clock {

	@Override
	public TimeZone getTimeZone() {
		return TimeZone.getDefault();
	}

	@Override
	public TimePoint now() {
		return LocalTimePoint.fromUniversalTime(
				System.currentTimeMillis(),
				getTimeZone()
				);
	}

}
