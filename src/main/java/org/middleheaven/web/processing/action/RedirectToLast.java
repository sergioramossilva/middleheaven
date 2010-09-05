package org.middleheaven.web.processing.action;

import org.middleheaven.web.processing.HttpContext;
import org.middleheaven.web.processing.Outcome;

public class RedirectToLast implements OutcomeResolver {

	@Override
	public Outcome resolveOutcome(OutcomeStatus status, HttpContext context) {
		return new Outcome(
				status,
				context.getRefererUrl().toString(),
				true,
				null
		);
	}

}
