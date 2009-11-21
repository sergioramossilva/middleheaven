package org.middleheaven.web.processing;

public interface HttpFilterChain {

	public void doChain(HttpContext context) throws HttpProcessException;

	public void interruptWithOutcome(Outcome outcome);
	
}
