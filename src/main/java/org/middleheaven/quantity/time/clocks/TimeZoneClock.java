package org.middleheaven.quantity.time.clocks;


import org.middleheaven.quantity.time.Clock;
import org.middleheaven.quantity.time.EpocTimePoint;
import org.middleheaven.quantity.time.TimePoint;
import org.middleheaven.quantity.time.TimeUtils;
import org.middleheaven.quantity.time.TimeZone;
import org.middleheaven.quantity.time.WrappedClock;


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

