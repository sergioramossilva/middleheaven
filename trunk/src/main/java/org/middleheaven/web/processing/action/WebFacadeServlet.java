package org.middleheaven.web.processing.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.logging.Logging;
import org.middleheaven.ui.ContextScope;

public final class WebFacadeServlet extends HttpServlet {

	public final void doPut(HttpServletRequest request, HttpServletResponse response)throws IOException, ServletException{
		doService(HttpMethod.PUT,request,response);
	}

	public final void doDelete(HttpServletRequest request, HttpServletResponse response)throws IOException, ServletException{
		doService(HttpMethod.DELETE,request,response);
	}

	public final void doPost(HttpServletRequest request, HttpServletResponse response)throws IOException, ServletException{
		doService(HttpMethod.POST,request,response);
	}

	public final void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		doService(HttpMethod.GET,request,response);
	}

	private void doService(HttpMethod service, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

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
				// 404 resource not found
				response.sendError(404);
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
		} catch (RuntimeException e){
			Logging.getBook("web").error("Unexpected error" , e);
			response.sendError(500); // serve error
		}
	}

	private String parseEnviromentName(HttpServletRequest request) {
		StringBuilder requestURL = new StringBuilder(request.getRequestURL());
		
		String contextPath = request.getContextPath();
		int pos = requestURL.indexOf(contextPath)+contextPath.length();
		int endpos = requestURL.lastIndexOf("/");
		
		
		return requestURL.substring(pos+1);
	}

	private CharSequence stripedRequestPath(HttpServletRequest request ) {
		StringBuilder requestURL = new StringBuilder(request.getRequestURL());
	
		String contextPath = request.getContextPath();
		// remove context from the start and suffix from end
		final int afterContext = requestURL.indexOf(contextPath)>0 ? requestURL.indexOf(contextPath) + contextPath.length(): 0;
		
		String[] url = requestURL.substring(afterContext, requestURL.length() - requestURL.lastIndexOf(".",afterContext)).split("\\.") ;
		
		if ( url.length>1){
			request.setAttribute("action", url[1]);
		}
		return url[0];
	}
}
