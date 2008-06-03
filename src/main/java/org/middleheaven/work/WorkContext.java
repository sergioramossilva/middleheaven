package org.middleheaven.work;

public interface WorkContext {

	/**
	 * 
	 * @return <code>true</code> if the work is running under a scheduled context.<code>false</code> otherwise.
	 */
	public boolean isScheduled();
}
