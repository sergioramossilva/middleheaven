package org.middleheaven.work.scheduled;

import org.middleheaven.quantity.time.Clock;
import org.middleheaven.quantity.time.TimeContext;
import org.middleheaven.quantity.time.TimePoint;
import org.middleheaven.quantity.time.clocks.ClockTickListener;
import org.middleheaven.quantity.time.clocks.StaticClock;
import org.middleheaven.work.Work;

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
		
	}
	
	private static class WorkTask implements ClockTickListener{

		Work work;
		
		public WorkTask(Work work) {
			this.work = work;
		}

		@Override
		public void onTick(TimePoint point) {
			
			work.execute(new AlarmScheduledWorkContext(StaticClock.forTime(point)));
		}
		
	}
}
