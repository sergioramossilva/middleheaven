/**
 * 
 */
package org.middleheaven.application;


/**
 * 
 */
public interface ModuleActivator {

	
	public ModuleVersion getModuleVersion();
	

	/**
	 * start the module. This callback method is invoked during the application init cycle.
	 * @param context the application context
	 */
	public void start(ApplicationContext context);
	

	/**
	 * stop the module. This callback method is invoked during the application stop cycle.
	 * @param context the application context
	 */
	public void stop(ApplicationContext context);
}
