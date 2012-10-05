/**
 * 
 */
package org.middleheaven.application;

/**
 * 
 */
public interface ListenableModuleActivator extends ModuleActivator {

	
	public void addModuleActivatorListener(ModuleActivatorListener listener);
	
	public void removeModuleActivatorListener(ModuleActivatorListener listener);
}
