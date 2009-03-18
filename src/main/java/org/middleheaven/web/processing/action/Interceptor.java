package org.middleheaven.web.processing.action;

public interface Interceptor {

	public void intercept(WebContext context, InterceptorChain chain);
	
}
