package org.middleheaven.web.processing.action;

import org.middleheaven.web.processing.HttpContext;
import org.middleheaven.web.processing.Outcome;

public interface InterceptorChain {

	
	public void doChain(HttpContext context);
	
	
	public void interruptWithOutcome(Outcome outcome);

	
	
}
