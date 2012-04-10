package org.middleheaven.application;

import java.util.Collection;
import java.util.Map;

public interface ApplicationInfo {

	
	public Collection<ModuleListener> getModules();
	public Map<String, Object> getAttributes();
	public <T> T getAttribute(String name, Class<T> type); 
}
