package org.middleheaven.util.measure.time.clocks;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.middleheaven.util.measure.time.TimePoint;


public class ClockTicked {

	
	protected ClockTicked(){}
	
	Set<ClockTickListener> listeners = new CopyOnWriteArraySet<ClockTickListener>();
	
	public void addClockTickListener (ClockTickListener listener){
		listeners.add(listener);
	}
	
	public void removeClockTickListener (ClockTickListener listener){
		listeners.remove(listener);
	}

	public void tick(TimePoint point ) {
		for (ClockTickListener c : listeners){
			c.onTick(point);
		}
	}
}
