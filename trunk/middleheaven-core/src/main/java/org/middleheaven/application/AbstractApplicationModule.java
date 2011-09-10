package org.middleheaven.application;

import org.middleheaven.util.Version;

public abstract class AbstractApplicationModule implements ApplicationModule {

	private ApplicationID applicationID;
	
	public AbstractApplicationModule(String applicationName, Version applicationVersion){
		this.applicationID = new ApplicationID(applicationName,applicationVersion);
	}
	
	public AbstractApplicationModule(ApplicationID applicationID){
		this.applicationID = applicationID;
	}
	
	
	@Override
	public final ApplicationID getApplicationID() {
		return applicationID;
	}


	@Override
	public ModuleID getModuleID() {
		return new ModuleID(this.getClass().getSimpleName() , this.getApplicationID().getVersion());
	}


}
