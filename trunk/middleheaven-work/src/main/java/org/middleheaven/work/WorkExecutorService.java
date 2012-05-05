package org.middleheaven.work;

/**
 * Service to execute units of work.
 */
public interface WorkExecutorService {
	
	/**
	 * Execute the unit of work.
	 * @param work
	 */
	public void executeWork(Work work);
}
