package org.middleheaven.util.measure.time.clocks;


import org.middleheaven.util.measure.time.Clock;
import org.middleheaven.util.measure.time.EpocTimePoint;
import org.middleheaven.util.measure.time.TimePoint;
import org.middleheaven.util.measure.time.TimeUtils;
import org.middleheaven.util.measure.time.TimeZone;
import org.middleheaven.util.measure.time.WrappedClock;


public class TimeZoneClock extends WrappedClock {


	TimeZone timeZone;
	
	public TimeZoneClock(TimeZone timeZone, Clock referenceClock){
		super(referenceClock);
		this.timeZone = timeZone;
	}

	public void setReferenceClock (Clock referenceClock){
		super.setReferenceClock(referenceClock);
	}
	
	@Override
	public TimeZone getTimeZone() {
		return timeZone;
	}

	@Override
	public TimePoint getTime() {
		return calculateTimeFromReference(getReferenceClock().getTime());
	}

	@Override
	protected TimePoint calculateTimeFromReference(TimePoint referenceTime) {
		return new EpocTimePoint(TimeUtils.convertLocalTime(
				referenceTime.milliseconds(),
				getReferenceClock().getTimeZone(),
				this.timeZone)
		);
	}
}

