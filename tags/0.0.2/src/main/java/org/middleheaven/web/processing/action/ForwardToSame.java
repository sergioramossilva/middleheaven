package org.middleheaven.web.processing.action;

import org.middleheaven.web.processing.HttpContext;
import org.middleheaven.web.processing.Outcome;

public class ForwardToSame implements OutcomeResolver {

	
	public ForwardToSame (){}
	
	@Override
	public Outcome resolveOutcome(OutcomeStatus status, HttpContext context) {
		return new Outcome(status,  context.getRequestUrl().getContexlessPath() + context.getRequestUrl().getFilename() , "text/html");
	}

}
