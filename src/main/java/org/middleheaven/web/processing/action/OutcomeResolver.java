package org.middleheaven.web.processing.action;

import org.middleheaven.web.processing.HttpContext;
import org.middleheaven.web.processing.Outcome;

public interface OutcomeResolver {

	
	public Outcome resolveOutcome(OutcomeStatus status, HttpContext context);
}
