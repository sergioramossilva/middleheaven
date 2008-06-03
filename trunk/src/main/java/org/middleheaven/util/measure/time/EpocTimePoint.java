package org.middleheaven.util.measure.time;

import java.util.Date;

public class EpocTimePoint implements TimePoint  {


	private final long timeFromEpoc;
	
	public EpocTimePoint(long timeFromEpoc) {
		this.timeFromEpoc = timeFromEpoc;
	}

	@Override
	public long milliseconds() {
		return timeFromEpoc;
	}

	@Override
	public TimeInterval until(TimePoint other) {
		 return new TimeInterval(this,other);
	}

	@Override
	public int compareTo(TimePoint other) {
		return (int)(this.timeFromEpoc - other.milliseconds());
	}

	public String toString(){
		return new Date(this.timeFromEpoc).toString();
	}
}
