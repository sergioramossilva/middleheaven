package org.middleheaven.application;

/**
 * 
 */
public interface ModuleListener {

	/**
	 * 
	 * @param module
	 * @return <code>true</code> if this object should process events from the given module.
	 */
	public boolean isListenerOf (ModuleVersion module);
	
	public void onStart(ModuleVersion module , ApplicationContext context);
	
	public void onStop(ModuleVersion module , ApplicationContext context);
	
}
