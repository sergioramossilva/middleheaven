package org.middleheaven.process.web.server.action;

import org.middleheaven.process.web.server.HttpServerContext;
import org.middleheaven.process.web.server.Outcome;

public interface ActionInterceptorsChain {

	
	public void doChain(HttpServerContext context);
	
	
	public void interruptWithOutcome(Outcome outcome);

	
	
}
