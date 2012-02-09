package org.middleheaven.application;

import org.middleheaven.util.Version;

public abstract class MainApplicationModule extends AbstractApplicationModule {


	public MainApplicationModule(String applicationName, Version applicationVersion){
		super(applicationName,applicationVersion);
	}
	
	public MainApplicationModule(ApplicationID applicationID){
		super(applicationID);
	}
	
	@Override
	public ModuleID getModuleID() {
		return new ModuleID(this.getApplicationID().getIdentifier(), this.getApplicationID().getVersion());
	}

	
	public String toString(){
		return this.getModuleID().toString();
	}
}
