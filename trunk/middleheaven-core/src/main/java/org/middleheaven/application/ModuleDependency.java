/**
 * 
 */
package org.middleheaven.application;

/**
 * 
 */
public class ModuleDependency {

	

	private boolean required = true;
	private ModuleVersion moduleVersion;
	
	
	public ModuleDependency(ModuleVersion moduleVersion, boolean required) {
		super();
		this.moduleVersion = moduleVersion;
		this.required = required;
	}
	
	/**
	 * Obtains {@link Module}.
	 * @return the dependendableModule
	 */
	public ModuleVersion getDependencyModuleVersion() {
		return moduleVersion;
	}
	/**
	 * Obtains {@link boolean}.
	 * @return the required
	 */
	public boolean isRequired() {
		return required;
	}
	
	
	
}
