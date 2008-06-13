package org.middleheaven.application;

import org.middleheaven.util.Version;

public interface ApplicationContext {

	
	public void addModule(ApplicationModule module);
	
	public boolean isCompatibleModulePresent(String identifier, Version version);
}
