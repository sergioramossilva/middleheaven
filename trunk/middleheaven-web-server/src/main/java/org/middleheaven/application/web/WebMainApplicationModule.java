package org.middleheaven.application.web;

import org.middleheaven.application.ApplicationID;
import org.middleheaven.application.MainApplicationModule;
import org.middleheaven.core.wiring.annotations.Wire;
import org.middleheaven.core.wiring.service.Service;
import org.middleheaven.process.web.server.HttpServerService;

/**
 * A Web Application main module.
 * The Web Application has access to the {@link HttpServerService}.
 */
public abstract class WebMainApplicationModule extends MainApplicationModule {

	private HttpServerService serverService;
	
	public WebMainApplicationModule(ApplicationID applicationID) {
		super(applicationID);
	}

	/**
	 * Wiring point for the environments {@link HttpServerService}.
	 * 
	 * @param serverService the environments {@link HttpServerService}.
	 */
	@Wire
	public final void setHttpServerService(HttpServerService serverService){
		this.serverService = serverService;
	}
	
	protected final HttpServerService getHttpServerService(){
		return serverService;
	}
	
	

}
