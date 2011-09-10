package org.middleheaven.work.scheduled;

import org.middleheaven.work.Work;

public interface ScheduledWorkExecutor {

	/**
	 * Schedule a <code>Work</code> to be executed as defined in the <code>Chronogram</code>
	 * @param work
	 * @param cronogram
	 */
	public void scheduleWork(Work work, Schedule cronogram);
}
