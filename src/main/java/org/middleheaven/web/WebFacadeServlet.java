package org.middleheaven.web;

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
		doService(HttpServices.PUT,request,response);
	}

	public final void doDelete(HttpServletRequest request, HttpServletResponse response)throws IOException, ServletException{
		doService(HttpServices.DELETE,request,response);
	}

	public final void doPost(HttpServletRequest request, HttpServletResponse response)throws IOException, ServletException{
		doService(HttpServices.POST,request,response);
	}

	public final void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		doService(HttpServices.GET,request,response);
	}

	private void doService(HttpServices service, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

		try {
			
			// Determine the current environment from URL information
			
			String enviromentName = this.parseEnviromentName(request.getRequestURL());
			
			final String suffix = getServletConfig().getInitParameter("suffix");

			StringBuffer requestURL = request.getRequestURL();

			if (suffix!=null && requestURL.indexOf(suffix) != requestURL.length() - suffix.length()){
				Logging.getBook("web").warn("Sufix missmatch ("+ requestURL +")");
				response.sendError(501); // service not implemented
				return;
			} 

			WebContext context = new WebContext(service,request,response);

			// resolve request location
			context.setAttribute(ContextScope.REQUEST, "locale", request.getLocale());

			WebApplication webApplication = context.getAttribute(ContextScope.APPLICATION , "_webApplication", WebApplication.class);


			// resolve mapped WebCommand from url

			WebCommandMappingService mapper = ServiceRegistry.getService(WebCommandMappingService.class);

			WebCommandMapping webCommandMapping = mapper.resolve(stripedRequestPath(request, suffix));

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
		
		if
		requestURL.substring(pos+1);
	}

	private CharSequence stripedRequestPath(HttpServletRequest request , String suffix ) {
		StringBuilder requestURL = new StringBuilder(request.getRequestURL());
		String end = ".";
		if ( suffix != null){
			end = end.concat(suffix);
		}
		// remove context from the start and suffix from end
		String[] url = requestURL.substring( requestURL.indexOf(request.getContextPath()) + request.getContextPath().length(), requestURL.length() - end.length()).split("\\.") ;
		
		if ( url.length>1){
			request.setAttribute("action", url[1]);
		}
		return url[0];
	}
}
