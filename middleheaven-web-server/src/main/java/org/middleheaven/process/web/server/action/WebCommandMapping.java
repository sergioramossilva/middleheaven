package org.middleheaven.process.web.server.action;

import org.middleheaven.process.web.server.HttpServerContext;
import org.middleheaven.process.web.server.Outcome;


public interface WebCommandMapping {


	public boolean matches(CharSequence url);
	
	public Outcome execute(HttpServerContext context);
	
	public Outcome resolveOutcome(String action, OutcomeStatus status, HttpServerContext context);
	

}
