package org.middleheaven.work.scheduled;

import org.middleheaven.util.measure.time.TimePoint;

public interface Chronogram {

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
}
