/**
 * 
 */
package org.middleheaven.application;

import java.util.Collection;

import org.middleheaven.util.Version;

/**
 * 
 */
public interface ApplicationRegistry {

	/**
	 * 
	 * @return the application's version
	 */
	public Version getVersion();
	
	/**
	 * 
	 * @return the application's mnemonic id
	 */
	public String getApplicationId();
	
	/**
	 * @param module
	 */
	public void addModule(Module module);
	
	/**
	 * 
	 * @return the application's modules.
	 */
	public Collection<Module> getModules();
	
	public boolean isModulePresent(String name);
	
	public boolean isCompatibleModulePresent(ModuleVersion version);
	
	
	public void addModuleListener (ModuleListener listener);
	
	public void removeModuleListener (ModuleListener listener);

	public void addApplicationListener(ApplicationListener listener);
	
	public void removeApplicationListener(ApplicationListener listener);



	
	
}
