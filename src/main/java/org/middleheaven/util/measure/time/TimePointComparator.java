package org.middleheaven.util.measure.time;

import java.util.Comparator;

class TimePointComparator implements Comparator<TimePoint> {

	@Override
	public int compare(TimePoint a, TimePoint b) {
		return a.compareTo(b);
	}

}
