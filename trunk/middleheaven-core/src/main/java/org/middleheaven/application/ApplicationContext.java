package org.middleheaven.application;

import java.util.Collection;

import org.middleheaven.core.services.ServiceContext;


/**
 * The set of all modules of an application.
 * 
 */
public interface ApplicationContext {

	public ApplicationVersion getApplication();
	
	public Collection<Module> getModules();
	
	public boolean isModulePresent(String name);
	
	public boolean isCompatibleModulePresent(ModuleVersion version);
	
	public ServiceContext getServiceContext();
}
