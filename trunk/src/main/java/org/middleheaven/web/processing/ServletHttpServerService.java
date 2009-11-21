package org.middleheaven.web.processing;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.middleheaven.aas.old.AccessDeniedException;
import org.middleheaven.aas.old.AccessFailedException;
import org.middleheaven.logging.Logging;
import org.middleheaven.web.processing.action.HttpProcessIOException;
import org.middleheaven.web.processing.action.HttpProcessServletException;
import org.middleheaven.web.processing.action.RequestResponseWebContext;

// created directly on the WebContainerBoostrap
class ServletHttpServerService extends AbstractHttpServerService {



	public ServletHttpServerService(){

		addRenderingProcessorResolver("jsp",new DefaultJspRenderingProcessorResolver(),new UrlMapping(){

			@Override
			public boolean match(String url) {
				return true;
			}
			
		} );
	}
	
	void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

		if (isStopped()){
			Logging.getBook(this.getClass()).warn("HttpServerService is stopped.");
			response.sendError(HttpCode.NOT_FOUND.intValue()); 
			return;
		}

		if (!isAvailable()){
			Logging.getBook(this.getClass()).warn("HttpServerService is not available.");
			response.sendError(HttpCode.SERVICE_UNAVAILABLE.intValue()); 
			return;
		}

		// determine processor
		HttpProcessor processor = resolveControlProcessor(request.getRequestURI());

		if (processor == null){
			Logging.getBook(this.getClass()).warn("ControlProcessor has not found.");
			response.sendError(HttpCode.NOT_IMPLEMENTED.intValue()); 
			return;
		}

		try{
			RequestResponseWebContext context = new  RequestResponseWebContext(request,response);
			
			// execute processing
			this.doService(context, processor);

		}catch (AccessDeniedException e){
			Logging.getBook(this.getClass()).warn("Access denied to " + request.getRequestURI());
			response.sendError(HttpCode.FORBIDDEN.intValue());
		}catch (HttpProcessIOException e){
			throw e.getIOException();
		}catch (HttpProcessServletException e){
			throw e.getServletException();
		}catch (Throwable e){
			throw new ServletException(e);
		} 
	}

	protected void doOnChainEnd(HttpContext ctx,HttpProcessor processor , Outcome outcome) throws HttpProcessException{
		
		RequestResponseWebContext context = (RequestResponseWebContext)ctx;
		HttpServletRequest request = context.getRequest();
		HttpServletResponse response = context.getResponse();
		
		if (outcome == null){
			 outcome = processor.process(context);
		}

		try{
			if(outcome == null){
				Logging.getBook(this.getClass()).warn("Outcome is null for " + request.getRequestURI());
				response.sendError(HttpCode.INTERNAL_SERVER_ERROR.intValue());
			} else if (outcome.isTerminal()){
				Logging.getBook(this.getClass()).debug("Outcome is terminal for " + request.getRequestURI());
				return; // do not process view. The response is already done written
			} else if (outcome.isError){
				response.sendError(outcome.getHttpCode().intValue());
			}else if (outcome.isDoRedirect()){
				if (outcome.getHttpCode().equals(HttpCode.MOVED_PERMANENTLY)){
					response.setStatus(HttpCode.MOVED_PERMANENTLY.intValue());
					response.setHeader( "Location", addContextPath(request.getContextPath(), outcome.getUrl()));
					response.setHeader( "Connection", "close" );
				} else {
					response.sendRedirect(addContextPath(request.getContextPath(), outcome.getUrl()));
				}
				
			} else {
				RenderingProcessor render = this.resolverRenderingProcessor(outcome.getUrl());
	
				render.process(context, outcome);
			}
		} catch (IOException e){
			throw  new HttpProcessIOException(e);
		}
	}
	
	private String addContextPath(String ctx, String url){
		if (url.startsWith("/")){
			return ctx.concat(url);
		} else {
			return url;
		}
	} 



}
