package org.middleheaven.process.web.server.action;

import org.middleheaven.process.web.server.HttpServerContext;
import org.middleheaven.process.web.server.Outcome;

public interface OutcomeResolver {

	
	public Outcome resolveOutcome(OutcomeStatus status, HttpServerContext context);
}
