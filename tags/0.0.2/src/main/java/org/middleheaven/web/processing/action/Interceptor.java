package org.middleheaven.web.processing.action;

import org.middleheaven.web.processing.HttpContext;

public interface Interceptor {

	public void intercept(HttpContext context, InterceptorChain chain);
	
}
