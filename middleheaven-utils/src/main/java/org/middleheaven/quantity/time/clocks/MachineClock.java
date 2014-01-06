package org.middleheaven.quantity.time.clocks;


import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.middleheaven.quantity.time.EpocTimePoint;
import org.middleheaven.quantity.time.TimePoint;
import org.middleheaven.quantity.time.TimeZone;
import org.middleheaven.reflection.ReflectionException;
import org.middleheaven.reflection.inspection.Introspector;
import org.middleheaven.runtime.ShutdownHook;
import org.middleheaven.runtime.ShutdownHooks;

/**
 * The clock used by the machine. This is the standard system clock with
 * cadence of 1 and default time zone. 
 *
 */
public class MachineClock extends Clock {

	private static final MachineClock ME = new MachineClock();

	public static MachineClock getInstance(){
		return ME;
	}

	private MachineClock (){}

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

	final Map<Schedule, ClockTicked> timers = new  HashMap<Schedule, ClockTicked>();

	private Timer timer;


	@Override
	protected  ClockTicked schedule(Schedule chronogram) {

		ClockTicked ticked = timers.get(chronogram);
		if (ticked == null){

			// create only when first time is created
			if (timer == null) {
				timer = new Timer("Machine Clock Alert Timer");
				ShutdownHooks.addShutdownHook(new MachineClockShutdownHook(timer));
			}
			
			ticked = new TimerClockTicked(chronogram, this);
			timers.put(chronogram, ticked);
		}
		return ticked;
	}



	private class TimerClockTicked extends ClockTicked{

		Schedule chronogram;

		public TimerClockTicked(Schedule chronogram,Clock clock) {
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

			if (chronogram.getStartTime()==null){
				timer.schedule(new TickTimerTask(translatingMethod, translatingObject), 0, chronogram.repetitionPeriod().milliseconds());
			} else {
				timer.schedule(new TickTimerTask(translatingMethod, translatingObject), new Date(chronogram.getStartTime().getMilliseconds()), chronogram.repetitionPeriod().milliseconds());
			}
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

				TimePoint t = Introspector.of(translatingMethod).invoke(
						TimePoint.class,
						this.translatingObject, 
						new EpocTimePoint(this.scheduledExecutionTime())
						);

				if (chronogram.contains(t)){
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
