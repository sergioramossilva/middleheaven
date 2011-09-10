package org.middleheaven.process.web.server.action;

import org.middleheaven.process.web.server.HttpServerContext;
import org.middleheaven.process.web.server.Outcome;

public class RedirectToLast implements OutcomeResolver {

	@Override
	public Outcome resolveOutcome(OutcomeStatus status, HttpServerContext context) {
		return new Outcome(
				status,
				context.getRequest().getRefererUrl().toString(),
				true,
				null
		);
	}

}
