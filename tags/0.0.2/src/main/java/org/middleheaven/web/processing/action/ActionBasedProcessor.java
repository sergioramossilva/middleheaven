package org.middleheaven.web.processing.action;

import org.middleheaven.ui.ContextScope;
import org.middleheaven.web.processing.AbstractHttpProcessor;
import org.middleheaven.web.processing.HttpCode;
import org.middleheaven.web.processing.HttpContext;
import org.middleheaven.web.processing.HttpProcessException;
import org.middleheaven.web.processing.Outcome;

public class ActionBasedProcessor extends AbstractHttpProcessor{

	private WebCommandMappingService mapper;
	
	public ActionBasedProcessor (WebCommandMappingService mapper){
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
				return new Outcome(BasicOutcomeStatus.ERROR, HttpCode.NOT_FOUND);
			} 
			
			
			return webCommandMapping.execute(context);
			
		} catch ( ActionHandlerNotFoundException e ){
			return new Outcome(BasicOutcomeStatus.ERROR, HttpCode.NOT_IMPLEMENTED);
		}

	}

	private CharSequence stripedRequestPath(HttpContext context) {
		StringBuilder requestURL = new StringBuilder(context.getRequestUrl().toString());
		
		// remove context from the start and suffix from end
		String strip;
		final int start;
		if(context.getContextPath().isEmpty()){ 
			// running as root
			start = requestURL.indexOf("/", requestURL.indexOf("://")+4)+1;
		
		} else {
			// running on context
			
			start = requestURL.indexOf(context.getContextPath()) + context.getContextPath().length()+1;
		}
		
	
		final int end = requestURL.lastIndexOf(".");
		if(end>0){
			strip = requestURL.substring( start, end);
		} else {
			strip = requestURL.substring(start);
		}
		
		
		String[] url = strip.split("\\."); 
		if ( url.length>1){
			context.setAttribute(ContextScope.REQUEST, "action", url[1]);
		}
		return url[0];
	}

}
