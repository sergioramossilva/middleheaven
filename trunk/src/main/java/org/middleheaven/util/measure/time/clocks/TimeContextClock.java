package org.middleheaven.util.measure.time.clocks;

import org.middleheaven.util.measure.time.Clock;
import org.middleheaven.util.measure.time.TimeContext;
import org.middleheaven.util.measure.time.WrappedClock;

public final class TimeContextClock extends WrappedClock{

	public TimeContextClock() {
		super(null);
	}
	
	protected Clock getReferenceClock(){
		return TimeContext.getTimeContext().getReferenceClock();
	}
	

}
