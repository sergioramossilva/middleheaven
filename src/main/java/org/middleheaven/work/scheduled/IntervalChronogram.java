package org.middleheaven.work.scheduled;

import org.middleheaven.util.Interval;
import org.middleheaven.util.measure.time.Period;
import org.middleheaven.util.measure.time.TimePoint;

public class IntervalChronogram implements Chronogram{

	Interval<TimePoint> interval;
	Period period;
	
	public IntervalChronogram(TimePoint start, TimePoint end, Period period){
	    this.interval = Interval.between(start, end);
		this.period = period;
	}
	
	@Override
	public TimePoint getEndTime() {
		return interval.end();
	}

	@Override
	public TimePoint getStartTime() {
		return interval.start();
	}

	@Override
	public boolean include(TimePoint point) {
		return interval.contains(point,true,false) ;
	}

	@Override
	public Period repetitionPeriod() {
		return period;
	}

	@Override
	public boolean isLimited() {
		return interval.end()!=null;
	}

}
