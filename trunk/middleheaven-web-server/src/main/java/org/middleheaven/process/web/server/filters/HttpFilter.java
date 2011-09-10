package org.middleheaven.process.web.server.filters;

import org.middleheaven.process.web.server.HttpServerContext;

/**
 * Filter is applied before the main processing is made.
 */
public interface HttpFilter {

	/**
	 * Contains the logic to be executed. A {@code HttpFilterChain} is passed 
	 * so the filter can control if it passes control to que next filter or not.
	 * 
	 * @param context the context where this filter will be executed;
	 * @param chain the chain of filters.
	 */
	public void doFilter(HttpServerContext context, HttpFilterChain chain);
}
