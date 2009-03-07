package org.middleheaven.work.scheduled;

import org.middleheaven.quantity.time.Clock;
import org.middleheaven.quantity.time.WrappedClock;
import org.middleheaven.quantity.time.clocks.ClockTickListener;

/**
 * Clock that warns when a determinated timepoint has arrived. 
 * The time elapsed is determinated by another clock, the reference clock
 * <code>AlarmClock</code> can be used to implements scheduling 
 * 
 */
public class AlarmClock extends WrappedClock {

	
	public AlarmClock(Clock referenceClock) {
		super(referenceClock);
	}

	public void addClockTickListener (ClockTickListener listener, Schedule schedule){
		schedule(schedule,this).addClockTickListener(listener);
	}
	
	public void removeClockTickListener (ClockTickListener listener,Schedule schedule){
		schedule(schedule,this).removeClockTickListener(listener);
	}


	
}
