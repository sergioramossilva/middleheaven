package org.middleheaven.web;

public interface InterceptorChain {

	
	public void doChain(WebContext context);
	
	public void interruptWithError(int errorCode);
	public void interruptAndRedirect(String url);
	
	
}
