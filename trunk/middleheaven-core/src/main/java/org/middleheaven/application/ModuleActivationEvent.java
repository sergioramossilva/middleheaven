/**
 * 
 */
package org.middleheaven.application;

/**
 * 
 */
public class ModuleActivationEvent {

	
	private ModuleActivator activator;
	
	public ModuleActivationEvent(ModuleActivator activator) {
		super();
		this.activator = activator;
	}
	
	/**
	 * Obtains {@link ModuleActivator}.
	 * @return the activator
	 */
	public ModuleActivator getActivator() {
		return activator;
	}
	/**
	 * Atributes {@link ModuleActivator}.
	 * @param activator the activator to set
	 */
	public void setActivator(ModuleActivator activator) {
		this.activator = activator;
	}
	
	
}
