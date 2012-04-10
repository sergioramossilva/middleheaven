package org.middleheaven.application.web;

import org.middleheaven.application.ApplicationContext;
import org.middleheaven.application.Module;
import org.middleheaven.process.web.server.HttpServerService;

/**
 * A Web Application main module.
 * 
 * A WebModule has access to a {@link HttpServerService}.
 */
public abstract class WebModule implements Module {

	
	private HttpServerService serverService;

	
	public WebModule() {}

	/**
	 * 
	 * {@inheritDoc}
	 */
	protected final HttpServerService getHttpServerService(){
		return serverService;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public final void start(ApplicationContext context) {

		serverService = context.getServiceContext().getService(HttpServerService.class);

		configurateModule(context);
	}
	
	/**
	 * @param context
	 */
	protected abstract void configurateModule(ApplicationContext context);



}
