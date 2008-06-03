package org.middleheaven.util.measure.time.clocks;

import java.util.TimeZone;

import org.middleheaven.util.measure.time.Clock;
import org.middleheaven.util.measure.time.TimePoint;

public class StaticClock implements Clock {


	TimeZone timeZone;

	TimePoint point;
	

	@Override
	public TimeZone getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}
	
	@Override
	public TimePoint now() {
		return point;
	}

	public void setTime(TimePoint point){
		this.point = point;
	}
}
