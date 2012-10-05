/**
 * 
 */
package org.middleheaven.application;

import java.util.Collection;

/**
 * Represents a process for discovering application modules. 
 */
public interface ModuleScanner {

	/**
	 * Scans for modules and adds them to {@code modules}.
	 * @param modules the found modules
	 */
	public void scan(Collection<Module> modules);
}
