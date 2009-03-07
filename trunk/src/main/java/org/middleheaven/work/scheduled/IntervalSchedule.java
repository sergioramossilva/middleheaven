package org.middleheaven.work.scheduled;

import org.middleheaven.quantity.time.Period;
import org.middleheaven.quantity.time.TimePoint;
import org.middleheaven.util.Interval;

public class IntervalSchedule implements Schedule{

	Interval<TimePoint> interval;
	Period period;
	
	public static IntervalSchedule schedule(TimePoint start, TimePoint end, Period period){
		return new IntervalSchedule(start,end,period);
	}

	public static IntervalSchedule schedule(TimePoint start,Period period){
		return new IntervalSchedule(start,null,period);
	}
	
	protected IntervalSchedule(TimePoint start, TimePoint end, Period period){
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
