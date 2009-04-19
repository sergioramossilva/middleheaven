package org.middleheaven.web.processing.action;

import org.middleheaven.web.processing.HttpContext;

public interface InterceptorChain {

	
	public void doChain(HttpContext context);
	
	public void interruptWithError(int errorCode);
	public void interruptAndRedirect(String url);
	
	
}
