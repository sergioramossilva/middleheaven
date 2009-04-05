package org.middleheaven.web;

import org.middleheaven.application.ApplicationContext;
import org.middleheaven.application.ApplicationID;
import org.middleheaven.application.MainApplicationModule;
import org.middleheaven.core.wiring.Wire;
import org.middleheaven.web.processing.HttpServerService;

public abstract class WebApplicationModule extends MainApplicationModule {

	private HttpServerService serverService;
	public WebApplicationModule(ApplicationID applicationID) {
		super(applicationID);
	}

	@Wire
	public void setHttpServerService(HttpServerService serverService){
		this.serverService = serverService;
	}
	
	@Override
	public final void load(ApplicationContext context) {
		doLoad(context, serverService);
	}
	
	@Override
	public final void unload(ApplicationContext context) {
		doUnload(context,serverService);
	}
	
	protected abstract void doLoad(ApplicationContext context,HttpServerService serverService);
	protected abstract void doUnload(ApplicationContext context,HttpServerService serverService);

	

}
