/**
 * 
 */
package org.middleheaven.application;

import java.util.Collection;

/**
 * Represents a process for discovering applications. 
 */
public interface ApplicationScanner {

	
	/**
	 * Scans for applications and adds them to {@code applications}.
	 * @param applications the found applications
	 */
	public void scan(Collection<Application> applications);
}
