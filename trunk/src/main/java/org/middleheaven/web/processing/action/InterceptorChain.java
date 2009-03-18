package org.middleheaven.web.processing.action;

public interface InterceptorChain {

	
	public void doChain(WebContext context);
	
	public void interruptWithError(int errorCode);
	public void interruptAndRedirect(String url);
	
	
}
