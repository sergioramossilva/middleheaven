/**
 * 
 */
package org.middleheaven.application;

import org.middleheaven.core.annotations.Service;

/**
 * Provides a register point for {@link ApplicationScanner}'s and 
 * a standard for inicialize the applications scan
 */
@Service
public interface ApplicationScanningService {

	
	public void addApplicationScanner(ApplicationScanner scanner);
	
	public void addModuleScanner(ModuleScanner scanner);
	
	public void scan();
}
