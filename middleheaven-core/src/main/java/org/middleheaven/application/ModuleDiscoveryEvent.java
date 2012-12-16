/**
 * 
 */
package org.middleheaven.application;

/**
 * 
 */
public class ModuleDiscoveryEvent {

	private ModuleVersion module;

	/**
	 * Constructor.
	 * @param module
	 */
	public ModuleDiscoveryEvent(ModuleVersion module) {
		this.module = module;
	}

	/**
	 * Obtains {@link Module}.
	 * @return the module
	 */
	public ModuleVersion getModuleVersion() {
		return module;
	}

	
	
}
