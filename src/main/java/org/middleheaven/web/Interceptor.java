package org.middleheaven.web;

public interface Interceptor {

	public void intercept(WebContext context, InterceptorChain chain);
	
}
