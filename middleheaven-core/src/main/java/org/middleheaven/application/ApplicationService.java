package org.middleheaven.application;

import org.middleheaven.core.wiring.service.Service;

@Service
public interface ApplicationService {

	
	public void addModuleListener (ModuleListener listener);
	
	public void removeModuleListener (ModuleListener listener);

	public void addApplicationListener(ApplicationListener listener);
	
	public void removeApplicationListener(ApplicationListener listener);

}
