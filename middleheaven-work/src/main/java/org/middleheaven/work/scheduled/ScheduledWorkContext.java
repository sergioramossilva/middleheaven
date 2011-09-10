package org.middleheaven.work.scheduled;

import org.middleheaven.quantity.time.clocks.Clock;
import org.middleheaven.work.WorkContext;

public interface ScheduledWorkContext extends WorkContext{

	
	public Clock clock();
}
