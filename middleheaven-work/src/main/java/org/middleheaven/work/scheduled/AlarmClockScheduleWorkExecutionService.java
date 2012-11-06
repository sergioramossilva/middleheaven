package org.middleheaven.work.scheduled;

import org.middleheaven.core.annotations.Service;
import org.middleheaven.logging.Logger;
import org.middleheaven.quantity.time.TimeContext;
import org.middleheaven.quantity.time.TimePoint;
import org.middleheaven.quantity.time.clocks.Clock;
import org.middleheaven.quantity.time.clocks.ClockTickListener;
import org.middleheaven.quantity.time.clocks.Schedule;
import org.middleheaven.quantity.time.clocks.StaticClock;
import org.middleheaven.quantity.time.clocks.alarm.AlarmClock;
import org.middleheaven.work.ScheduledWorkContext;
import org.middleheaven.work.Work;

/**
 * 
 */
@Service
public class AlarmClockScheduleWorkExecutionService implements ScheduleWorkExecutorService {

	AlarmClock clock;

	public AlarmClockScheduleWorkExecutionService(){
		this(TimeContext.getTimeContext().getReferenceClock());
	}

	public AlarmClockScheduleWorkExecutionService(Clock clock){
		this.clock = new AlarmClock(clock);
	}

	@Override
	public void execute(Work work, Schedule schedule) {
		clock.addClockTickListener(new WorkTask(work), schedule);
	}

	private static class AlarmScheduledWorkContext implements ScheduledWorkContext{

		Clock clock;

		public AlarmScheduledWorkContext(Clock clock) {
			super();
			this.clock = clock;
		}

		@Override
		public boolean isScheduled() {
			return true;
		}

		@Override
		public Clock clock() {
			return clock; 
		}

		@Override
		public ScheduledWorkContext getScheduledWorkContext() {
			return this;
		}

	}

	private static class WorkTask implements ClockTickListener{

		Work work;

		public WorkTask(Work work) {
			this.work = work;
		}

		@Override
		public void onTick(TimePoint point) {
			try{
				work.execute(new AlarmScheduledWorkContext(StaticClock.forTime(point)));
			}catch (Exception e){
				Logger.onBook("Schedule work").error(e,"Unexpected exception executing {0}", work.getClass());
			}
		}

	}
}
