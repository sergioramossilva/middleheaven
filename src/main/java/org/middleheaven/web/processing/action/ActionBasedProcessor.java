package org.middleheaven.web.processing.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.logging.Logging;
import org.middleheaven.ui.ContextScope;
import org.middleheaven.web.processing.AbstractHttpProcessor;
import org.middleheaven.web.processing.HttpErrors;
import org.middleheaven.web.processing.HttpProcessException;

public class ActionBasedProcessor extends AbstractHttpProcessor{

	@Override
	public void doProcess(HttpServletRequest request,HttpServletResponse response) throws HttpProcessException {
		try{
			try {

				// Determine the current environment from URL information

				String enviromentName = this.parseEnviromentName(request);

				StringBuffer requestURL = request.getRequestURL();

				WebContext context = new RequestResponseWebContext(request,response);

				// resolve request location
				context.setAttribute(ContextScope.REQUEST, "locale", request.getLocale());

				WebApplication webApplication = context.getAttribute(ContextScope.APPLICATION , "_webApplication", WebApplication.class);


				// resolve mapped WebCommand from url

				WebCommandMappingService mapper = ServiceRegistry.getService(WebCommandMappingService.class);

				WebCommandMapping webCommandMapping = mapper.resolve(stripedRequestPath(request));

				if (webCommandMapping==null){
					// resource not found
					response.sendError(HttpErrors.NOT_FOUND.errorCode());
					return;
				}


				Outcome outcome = webCommandMapping.execute(context);

				if (outcome.isTerminal()){
					return; // do nothing. The response is already done
				} else if (outcome.isError){
					response.sendError(Integer.parseInt(outcome.url));
				}else if (outcome.isDoRedirect()){
					response.sendRedirect(outcome.url);
				} else {
					request.getRequestDispatcher(outcome.url).include(request, response);
				}


			} catch ( ActionHandlerNotFoundException e ){
				response.sendError(501); // not implemented
			}
		}catch (IOException e){
			throw new HttpProcessIOException(e);
		}catch (ServletException e) {
			throw new HttpProcessServletException(e);
		} 
	}

	private String parseEnviromentName(HttpServletRequest request) {
		StringBuilder requestURL = new StringBuilder(request.getRequestURL());

		String contextPath = request.getContextPath();
		int pos = requestURL.indexOf(contextPath)+contextPath.length();
		int endpos = requestURL.lastIndexOf("/");


		return requestURL.substring(pos+1);
	}

	private CharSequence stripedRequestPath(HttpServletRequest request) {
		StringBuilder requestURL = new StringBuilder(request.getRequestURL());
		String end = ".";

		// remove context from the start and suffix from end
		String[] url = requestURL.substring( requestURL.indexOf(request.getContextPath()) + request.getContextPath().length(), requestURL.length() - end.length()).split("\\.") ;

		if ( url.length>1){
			request.setAttribute("action", url[1]);
		}
		return url[0];
	}

}
