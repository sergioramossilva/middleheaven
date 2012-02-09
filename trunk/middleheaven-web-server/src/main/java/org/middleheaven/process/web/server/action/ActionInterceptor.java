package org.middleheaven.process.web.server.action;

import org.middleheaven.process.web.server.HttpServerContext;

public interface ActionInterceptor {

	/**
	 * Executes code before, after or instead of the next chain call.
	 * @param context the server context
	 * @param chain the current {@link ActionInterceptor} chain.
	 * @see ActionInterceptorsChain
	 */
	public void intercept(HttpServerContext context, ActionInterceptorsChain chain);
	
}
