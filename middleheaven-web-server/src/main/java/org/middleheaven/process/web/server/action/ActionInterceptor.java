package org.middleheaven.process.web.server.action;

import org.middleheaven.process.web.server.HttpServerContext;

public interface ActionInterceptor {

	public void intercept(HttpServerContext context, ActionInterceptorsChain chain);
	
}
