package org.middleheaven.application;

/**
 * 
 */
public interface ModuleListener {


	public void onStart(ModuleVersion module , ApplicationContext context);
	
	public void onStop(ModuleVersion module , ApplicationContext context);
	
}
