package org.middleheaven.web.processing;

public interface HttpFilterChain {

	/**
	 * Delegates controls to the next filter or the main controller if no more filters exist in the chain
	 * @param context
	 * @throws HttpProcessException
	 */
	public void doChain(HttpContext context) throws HttpProcessException;

	/**
	 * Interrupt chain delegation and return with the passed outcome.
	 * @param outcome
	 */
	public void interruptWithOutcome(Outcome outcome);
	
}
