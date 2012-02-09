package org.middleheaven.process.web.server.action;

import org.middleheaven.process.web.HttpUrl;
import org.middleheaven.process.web.server.HttpServerContext;
import org.middleheaven.process.web.server.Outcome;

public class ForwardToSame implements OutcomeResolver {

	
	public ForwardToSame (){}
	
	@Override
	public Outcome resolveOutcome(OutcomeStatus status, HttpServerContext context) {
		final HttpUrl requestUrl = context.getRequestUrl();
		return new Outcome(status,  requestUrl.getContexlessPath() + requestUrl.getFilename() , "text/html");
	}

}
