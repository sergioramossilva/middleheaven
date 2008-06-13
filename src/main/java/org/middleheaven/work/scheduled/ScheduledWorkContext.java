package org.middleheaven.work.scheduled;

import org.middleheaven.util.measure.time.Clock;
import org.middleheaven.work.WorkContext;

public interface ScheduledWorkContext extends WorkContext{

	
	public Clock clock();
}
