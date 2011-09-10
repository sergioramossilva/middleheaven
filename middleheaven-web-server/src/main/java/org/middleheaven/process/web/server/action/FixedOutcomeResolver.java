package org.middleheaven.process.web.server.action;

import org.middleheaven.process.web.server.HttpServerContext;
import org.middleheaven.process.web.server.Outcome;

public class FixedOutcomeResolver implements OutcomeResolver {

	private Outcome outcome;

	public FixedOutcomeResolver (Outcome outcome){
		this.outcome = outcome;
	}
	
	@Override
	public Outcome resolveOutcome(OutcomeStatus status, HttpServerContext context) {
		if (status.equals(outcome.getStatus())) {
			return outcome;			
		}
		return null;
	}

}
