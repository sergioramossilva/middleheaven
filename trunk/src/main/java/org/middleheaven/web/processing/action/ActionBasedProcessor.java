package org.middleheaven.web.processing.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.ui.ContextScope;
import org.middleheaven.web.processing.AbstractHttpProcessor;
import org.middleheaven.web.processing.HttpContext;
import org.middleheaven.web.processing.HttpErrors;
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

			// Determine the current environment from URL information

			String enviromentName = this.parseEnviromentName(context);


			// resolve mapped WebCommand from url

			WebCommandMapping webCommandMapping = mapper.resolve(stripedRequestPath(context));

			if (webCommandMapping==null){
				// resource not found
				return new Outcome(OutcomeStatus.ERROR, HttpErrors.NOT_FOUND.errorCode());
			}


			return webCommandMapping.execute(context);


		} catch ( ActionHandlerNotFoundException e ){
			return new Outcome(OutcomeStatus.ERROR, HttpErrors.NOT_IMPLEMENTED.errorCode());
		}

	}

	private String parseEnviromentName(HttpContext context) {
		StringBuilder requestURL = new StringBuilder(context.getRequestUrl());

		String contextPath = context.getContextPath();
		int pos = requestURL.indexOf(contextPath)+contextPath.length();
		int endpos = requestURL.lastIndexOf("/");


		return requestURL.substring(pos+1);
	}

	private CharSequence stripedRequestPath(HttpContext context) {
		StringBuilder requestURL = new StringBuilder(context.getRequestUrl());
		String end = ".";

		// remove context from the start and suffix from end
		String[] url = requestURL.substring( requestURL.indexOf(context.getContextPath()) + context.getContextPath().length()+1, requestURL.length() - end.length()).split("\\.") ;

		if ( url.length>1){
			context.setAttribute(ContextScope.REQUEST, "action", url[1]);
		}
		return url[0];
	}

}
