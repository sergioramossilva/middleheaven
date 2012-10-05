/**
 * 
 */
package org.middleheaven.application;

/**
 * 
 */
public interface ModuleActivatorListener {

	
	public void onModuleStart(ModuleActivationEvent event);
	
	public void onModuleStop(ModuleActivationEvent event);
}
