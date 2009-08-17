package org.middleheaven.web.processing.action;

import org.middleheaven.ui.ContextScope;
import org.middleheaven.web.processing.AbstractHttpProcessor;
import org.middleheaven.web.processing.HttpContext;
import org.middleheaven.web.processing.HttpError;
import org.middleheaven.web.processing.HttpProcessException;
import org.middleheaven.web.processing.Outcome;

public class ActionBasedProcessor extends AbstractHttpProcessor{

	private WebCommandMappingService mapper;

	public ActionBasedProcessor(WebCommandMappingService mapper){
		this.mapper = mapper;
	}

	@Override
	public Outcome doProcess(HttpContext context) throws HttpProcessException {

		try {

			// resolve mapped WebCommand from url

			WebCommandMapping webCommandMapping = mapper.resolve(stripedRequestPath(context));
			
			Outcome outcome;
			
			if (webCommandMapping==null){
				// resource not found
				return new Outcome(BasicOutcomeStatus.ERROR, HttpError.NOT_FOUND.errorCode());
			} 
			
			
			return webCommandMapping.execute(context);
			
		} catch ( ActionHandlerNotFoundException e ){
			return new Outcome(BasicOutcomeStatus.ERROR, HttpError.NOT_IMPLEMENTED.errorCode());
		}

	}

	private CharSequence stripedRequestPath(HttpContext context) {
		StringBuilder requestURL = new StringBuilder(context.getRequestUrl());
		
		// remove context from the start and suffix from end
		
		int start = requestURL.indexOf(context.getContextPath()) + context.getContextPath().length()+1;
		int end = requestURL.length() == start ? start : requestURL.indexOf(".", start);
		String strip = requestURL.substring( start, end);

		String[] url = strip.split("\\."); 
		if ( url.length>1){
			context.setAttribute(ContextScope.REQUEST, "action", url[1]);
		}
		return url[0];
	}

}
