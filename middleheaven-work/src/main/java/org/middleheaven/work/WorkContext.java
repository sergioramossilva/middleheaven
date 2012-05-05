package org.middleheaven.work;

/**
 * The context of {@link Work} execution.
 */
public interface WorkContext {

	/**
	 * 
	 * @return <code>true</code> if the work is running under a scheduled context.<code>false</code> otherwise.
	 * If the context is running under a scheduled context it can be obtained using {@link #getScheduledWorkContext()}
	 */
	public boolean isScheduled();
	
	/**
	 * 
	 * @return the corresponding {@link ScheduledWorkContext} or {@code null} if no {@code ScheduledWorkContext} 
	 * is related to this context.
	 */
	public ScheduledWorkContext getScheduledWorkContext();
}
