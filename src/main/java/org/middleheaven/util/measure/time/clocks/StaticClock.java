package org.middleheaven.util.measure.time.clocks;


import java.util.HashMap;
import java.util.Map;

import org.middleheaven.util.measure.time.Clock;
import org.middleheaven.util.measure.time.EpocTimePoint;
import org.middleheaven.util.measure.time.TimePoint;
import org.middleheaven.util.measure.time.TimeZone;
import org.middleheaven.work.scheduled.Chronogram;

/**
 * A clock with fixed, immutable, time.
 *
 */
public class StaticClock extends Clock {


	TimeZone timeZone;
	TimePoint point;
	
	public static StaticClock forTime(TimePoint point){
		return new StaticClock(point);
	}
	
	public static StaticClock forTime(long miliseconds){
		return new StaticClock(new EpocTimePoint(miliseconds));
	}
	
	private StaticClock(TimePoint point) {
		this(point,TimeZone.getDefault());
	}
	
	public StaticClock(TimePoint point,TimeZone timeZone) {
		super();
		this.timeZone = timeZone;
		this.point = point;
	}
	
	@Override
	public TimeZone getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}
	
	@Override
	public TimePoint getTime() {
		return point;
	}

	public void setTime(TimePoint point){
		this.point = point;
		tick();
	}


	public void setLocalTime(long miliseconds){
		this.point = new EpocTimePoint(miliseconds);
		tick();
	}
	
	private void tick(){
		for (Chronogram c : timers.keySet()){
			if (c.include(this.point)){
				ClockTicked ticked = timers.get(c);
				if (ticked!=null){
					ticked.tick(this.point);
				}
			}
		}
	}
	/**
	 * @return 0 (zero) as this clock never changes is time
	 */
	@Override
	public double getCadence() {
		return 0;
	}
	Map<Chronogram, ClockTicked> timers = new  HashMap<Chronogram, ClockTicked>();
	
	@Override
	protected ClockTicked schedule(Chronogram chronogram, Clock clock) {
		ClockTicked ticked = timers.get(chronogram);
		if (ticked==null){
			ticked =new ClockTicked();
			timers.put(chronogram, ticked);
		}
		return ticked;
	}
	

}
