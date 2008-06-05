package org.middleheaven.util.measure.time.clocks;


import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.middleheaven.core.reflection.ReflectionException;
import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.util.measure.time.Clock;
import org.middleheaven.util.measure.time.EpocTimePoint;
import org.middleheaven.util.measure.time.TimePoint;
import org.middleheaven.util.measure.time.TimeZone;
import org.middleheaven.work.scheduled.Chronogram;

/**
 * The clock used by the machine. This is the standard system clock with
 * cadence of 1s/s and default time zone. 
 *
 */
public class MachineClock extends Clock {

	@Override
	public TimeZone getTimeZone() {
		return TimeZone.getDefault();
	}

	@Override
	public TimePoint getTime() {
		return new EpocTimePoint(System.currentTimeMillis());
	}

	/**
	 * @return 1 as this clock represent the machine clock with universal cadence
	 */
	@Override
	public double getCadence() {
		return 1;
	}

	Map<Chronogram, ClockTicked> timers = new  HashMap<Chronogram, ClockTicked>();
	
	Timer timer = new Timer();
	
	
	@Override
	protected ClockTicked schedule(Chronogram chronogram,Clock clock) {
		ClockTicked ticked = timers.get(chronogram);
		if (ticked==null){
			ticked =new TimerClockTicked(chronogram,clock);
			timers.put(chronogram, ticked);
		}
		return ticked;
	}
	

	
	private class TimerClockTicked extends ClockTicked{

		Chronogram chronogram;
	
		public TimerClockTicked(Chronogram chronogram,Clock clock) {
			this.chronogram = chronogram;
			Method translatingMethod=null;
			Object translatingObject=null;
			try {
				translatingMethod = clock.getClass().getDeclaredMethod("calculateTimeFromReference", TimePoint.class);
				translatingObject = clock;
			} catch (SecurityException e) {
				throw  ReflectionException.manage(e);
			} catch (NoSuchMethodException e) {
				try {
					translatingMethod = this.getClass().getDeclaredMethod("calculateTimeFromReference", TimePoint.class);
					translatingObject = this;
				} catch (SecurityException e1) {
					throw  ReflectionException.manage(e1);
				} catch (NoSuchMethodException e1) {
					throw  ReflectionException.manage(e1);
				}
			
			}
	
			timer.schedule(new TickTimerTask(translatingMethod, translatingObject), new Date(chronogram.getStartTime().milliseconds()), chronogram.repetitionPeriod().milliseconds());
		}
		
		protected TimePoint calculateTimeFromReference(TimePoint referenceTime) {
			return referenceTime;
		}
		
		private class TickTimerTask extends TimerTask {
			Method translatingMethod;
			Object translatingObject;
			
			public TickTimerTask(Method translatingMethod, Object translatingObject) {
				this.translatingMethod = translatingMethod;
				this.translatingObject = translatingObject;
			}

			@Override
			public void run() {
				
				TimePoint t = ReflectionUtils.invoke(TimePoint.class, this.translatingMethod , this.translatingObject , new EpocTimePoint(this.scheduledExecutionTime()));
				
				if (chronogram.include(t)){
					tick(t);
				}
				// cancel tick task if limit time as passed
				if (chronogram.isLimited() && chronogram.getEndTime().compareTo(t)<=0){
					timers.remove(chronogram);
					this.cancel();
				}
			}
			
		}
	}




}
