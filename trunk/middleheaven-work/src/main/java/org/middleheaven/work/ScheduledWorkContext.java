package org.middleheaven.work;

import org.middleheaven.quantity.time.clocks.Clock;

/**
 * An extension of {@link WorkContext} used when executing scheduled {@link Work}.
 */
public interface ScheduledWorkContext extends WorkContext{

	/**
	 * The clock used in the scheduling.
	 * @return the clock used in the scheduling.
	 */
	public Clock clock();
}
