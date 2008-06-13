package org.middleheaven.work.scheduled;

import org.middleheaven.work.Work;

public interface ScheduleWorkExecutorService {

	
	public void execute(Work work, Schedule schedule);
}
