package org.middleheaven.process.web.server.filters;

import org.middleheaven.process.web.HttpProcessException;
import org.middleheaven.process.web.server.HttpServerContext;
import org.middleheaven.process.web.server.Outcome;

public interface HttpFilterChain {

	/**
	 * Delegates controls to the next filter or the main controller if no more filters exist in the chain
	 * @param context
	 * @throws HttpProcessException
	 */
	public void doChain(HttpServerContext context) throws HttpProcessException;

	/**
	 * Interrupt chain delegation and return with the passed outcome.
	 * @param outcome
	 */
	public void interruptWithOutcome(Outcome outcome);
	
}
