package org.middleheaven.work.scheduled;

import org.middleheaven.quantity.time.Period;
import org.middleheaven.quantity.time.TimePoint;

public interface Schedule {

	/**
	 * 
	 * @return the first execution time
	 */
	public TimePoint getStartTime();
	
	/**
	 * 
	 * @return the last execution time, or <code>null</code> if there is no limit time
	 */
	public TimePoint getEndTime();

	public boolean include(TimePoint point);

	/**
	 * 
	 * @return <code>true</code> if this chronogram a stop time.<code>false</code> otherwise.
	 */
	public boolean isLimited();
	
	public Period repetitionPeriod();
}
