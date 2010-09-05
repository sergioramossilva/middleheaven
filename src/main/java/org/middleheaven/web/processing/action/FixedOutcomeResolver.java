package org.middleheaven.web.processing.action;

import org.middleheaven.web.processing.HttpContext;
import org.middleheaven.web.processing.Outcome;

public class FixedOutcomeResolver implements OutcomeResolver {

	private Outcome outcome;

	public FixedOutcomeResolver (Outcome outcome){
		this.outcome = outcome;
	}
	
	@Override
	public Outcome resolveOutcome(OutcomeStatus status, HttpContext context) {
		if (status.equals(outcome.getStatus())) {
			return outcome;			
		}
		return null;
	}

}
