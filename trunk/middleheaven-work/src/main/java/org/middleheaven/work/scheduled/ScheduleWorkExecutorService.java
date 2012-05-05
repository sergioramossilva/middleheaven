package org.middleheaven.work.scheduled;

import org.middleheaven.quantity.time.clocks.Schedule;
import org.middleheaven.work.Work;

/**
 * Service to execute units of work in a given schedule
 */
public interface ScheduleWorkExecutorService {

	/**
	 * Execute the work acording with the given {@link Schedule}.
	 * @param work the {@link Work} to execute.
	 * @param schedule the schecule that controls the work execution.
	 */
	public void execute(Work work, Schedule schedule);
}
