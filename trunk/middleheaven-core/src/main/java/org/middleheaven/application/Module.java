/**
 * 
 */
package org.middleheaven.application;


/**
 * 
 */
public interface Module {

	
	public ModuleVersion getModuleVersion();
	

	public void start(ApplicationContext context);
	

	public void stop(ApplicationContext context);
}
