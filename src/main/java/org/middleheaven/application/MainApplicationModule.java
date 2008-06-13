package org.middleheaven.application;

public abstract class MainApplicationModule implements ApplicationModule {

	private ApplicationID applicationID;
	
	public MainApplicationModule(ApplicationID applicationID){
		this.applicationID = applicationID;
	}
	
	@Override
	public final ApplicationID getApplicationID() {
		return applicationID;
	}

	@Override
	public ModuleID getModuleID() {
		return new ModuleID(this.applicationID.getIdentifier(), this.applicationID.getVersion());
	}

	@Override
	public final boolean isMain() {
		return true;
	}

}
