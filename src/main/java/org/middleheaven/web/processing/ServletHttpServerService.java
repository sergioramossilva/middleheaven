package org.middleheaven.web.processing;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.middleheaven.aas.old.AccessDeniedException;
import org.middleheaven.logging.Log;
import org.middleheaven.web.processing.action.HttpProcessIOException;
import org.middleheaven.web.processing.action.HttpProcessServletException;
import org.middleheaven.web.processing.action.RequestResponseWebContext;
import org.middleheaven.web.rendering.DefaultJspRenderingProcessorResolver;
import org.middleheaven.web.rendering.RenderingProcessor;

// created directly on the WebContainerBoostrap
class ServletHttpServerService extends AbstractHttpServerService {


	public ServletHttpServerService(){

		addRenderingProcessorResolver("jsp",new DefaultJspRenderingProcessorResolver(),UrlMapping.matchAll());
	}

	void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

		if (isStopped()){
			Log.onBookFor(this.getClass()).warn("HttpServerService is stopped.");
			response.sendError(HttpCode.NOT_FOUND.intValue()); 
			return;
		}

		if (!isAvailable()){
			Log.onBookFor(this.getClass()).warn("HttpServerService is not available.");
			response.sendError(HttpCode.SERVICE_UNAVAILABLE.intValue()); 
			return;
		}

		// determine processor
		HttpProcessor processor = resolveControlProcessor(request.getRequestURI());

		if (processor == null){
			Log.onBookFor(this.getClass()).warn("ControlProcessor has not found.");
			response.sendError(HttpCode.NOT_IMPLEMENTED.intValue()); 
			return;
		}

		try{
			RequestResponseWebContext context = new  RequestResponseWebContext(request,response);

			// execute processing
			Outcome outcome = this.doService(context, processor);


			if(outcome == null){
				Log.onBookFor(this.getClass()).warn("Outcome is null for " + request.getRequestURI());
				response.sendError(HttpCode.NOT_FOUND.intValue());
			} else if (outcome.isTerminal()){
				Log.onBookFor(this.getClass()).debug("Outcome is terminal for " + request.getRequestURI());
				return; // do not process view. The response is already done written
			} else if (outcome.isError){
				response.sendError(outcome.getHttpCode().intValue());
			}else if (outcome.isDoRedirect()){
				if (outcome.getHttpCode().equals(HttpCode.MOVED_PERMANENTLY)){
					response.setStatus(HttpCode.MOVED_PERMANENTLY.intValue());
					response.setHeader( "Location", addContextPath(request.getContextPath(), outcome.getParameterizedURL()));
					response.setHeader( "Connection", "close" );
				} else {
					response.sendRedirect(addContextPath(request.getContextPath(), outcome.getParameterizedURL()));
				}

			} else {
				RenderingProcessor render = this.resolverRenderingProcessor(outcome.getUrl());

				if (render == null){
					Log.onBookFor(this.getClass()).error("Render could not be found for " + outcome.getUrl());
					response.sendError(HttpCode.NOT_FOUND.intValue());
				} else {
					render.process(context, outcome);
				}
			}

		}catch (AccessDeniedException e){
			Log.onBookFor(this.getClass()).warn("Access denied to " + request.getRequestURI());
			response.sendError(HttpCode.FORBIDDEN.intValue());
		}catch (HttpProcessIOException e){
			throw e.getIOException();
		}catch (HttpProcessServletException e){
			throw e.getServletException();
		}catch (Throwable e){
			throw new ServletException(e);
		} 
	}

	protected Outcome doOnChainEnd(HttpContext context,HttpProcessor processor , Outcome outcome) throws HttpProcessException{

		if (outcome == null){
			outcome = processor.process(context);
		}

		 return outcome;
	}

	private String addContextPath(String ctx, String url){
		if (url.startsWith("/")){
			return ctx.concat(url);
		} else {
			return url;
		}
	}





}
