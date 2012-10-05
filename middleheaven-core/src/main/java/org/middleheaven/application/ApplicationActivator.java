/**
 * 
 */
package org.middleheaven.application;

/**
 * 
 */
public interface ApplicationActivator {
	
	public void init(ApplicationContext context);
	
	public void beforeModuleStart(ApplicationContext context);
	
	public void afterModuleStart(ApplicationContext context);
	
	public void stop(ApplicationContext context);
	
	public void beforeModuleStop(ApplicationContext context);
	
	public void afterModuleStop(ApplicationContext context);

}
