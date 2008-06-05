package org.middleheaven.util.measure.time.clocks;

import org.middleheaven.util.measure.time.TimePoint;

public interface ClockTickListener {

	public void onTick (TimePoint point);
}
