package org.middleheaven.web.processing;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.middleheaven.aas.old.AccessDeniedException;
import org.middleheaven.logging.Logging;
import org.middleheaven.web.processing.action.HttpProcessIOException;
import org.middleheaven.web.processing.action.HttpProcessServletException;
import org.middleheaven.web.processing.action.RequestResponseWebContext;

// created directly on the WebContainerBoostrap
class ServletHttpServerService implements HttpServerService {

	private final Map<String, HttpMapping> processorsMappings = new TreeMap<String, HttpMapping>();
	private final Map<String, HttpRenderingMapping> renderingMappings = new TreeMap<String, HttpRenderingMapping>();

	private boolean available = false;
	private boolean stopped = false;

	public ServletHttpServerService(){
		renderingMappings.put("jsp", new HttpRenderingMapping(new DefaultJspRenderingProcessorResolver(),new UrlMapping(){

			@Override
			public boolean match(String url) {
				return true;
			}
			
		} ));
	}
	
	@Override
	public void removeRenderingProcessorResolver(String resolverID) {
		renderingMappings.remove(resolverID);
	}
	
	@Override
	public void addRenderingProcessorResolver(String resolverID, RenderingProcessorResolver resolver, UrlMapping mapping) {
		renderingMappings.put(resolverID, new HttpRenderingMapping(resolver,mapping));
	}

	@Override
	public RenderingProcessor resolverRenderingProcessor(String url) {
		for (HttpRenderingMapping mapping : renderingMappings.values()){
			if (mapping.mapping.match(url)){
				return mapping.processor.resolve(url);
			}
		}
		return null;
	}



	@Override
	public HttpProcessor resolveControlProcessor(String url) {
		for (HttpMapping mapping : processorsMappings.values()){
			if (mapping.mapping.match(url)){
				return mapping.processor;
			}
		}
		return null;
	}

	@Override
	public void registerHttpProcessor(String processorID, HttpProcessor processor,UrlMapping mapping) {
		processorsMappings.put(processorID, new HttpMapping(processor,mapping));

	}

	@Override
	public void unRegisterHttpProcessor(String processorID) {
		processorsMappings.remove(processorID);
	}

	@Override
	public boolean isAvailable() {
		return available;
	}

	@Override
	public void setAvailable(boolean available) {
		this.available = available;
	}

	@Override
	public synchronized void stop() {
		this.available = false;
		this.stopped = true;
	}

	@Override
	public synchronized void start() {
		this.available = true;
	}


	void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

		if (stopped){
			Logging.getBook(this.getClass()).warn("HttpServerService is stopped.");
			response.sendError(HttpCode.NOT_FOUND.intValue()); 
			return;
		}

		if (!available){
			Logging.getBook(this.getClass()).warn("HttpServerService is not available.");
			response.sendError(HttpCode.SERVICE_UNAVAILABLE.intValue()); 
			return;
		}

		HttpProcessor processor = resolveControlProcessor(request.getRequestURI());

		if (processor == null){
			Logging.getBook(this.getClass()).warn("ControlProcessor has not found.");
			response.sendError(HttpCode.NOT_IMPLEMENTED.intValue()); 
			return;
		}


		// TODO apply filter

		try{
			RequestResponseWebContext context = new  RequestResponseWebContext(request,response);
			Outcome outcome = processor.process(context);

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
					response.setHeader( "Location", outcome.getUrl() );
					response.setHeader( "Connection", "close" );
				} else {
					response.sendRedirect(outcome.getUrl());
				}
				
			} else {
				RenderingProcessor render = this.resolverRenderingProcessor(outcome.getUrl());

				render.process(context, outcome);
			}

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

		// TODO apply filter
	}

	private class HttpMapping{

		public HttpProcessor processor;
		public UrlMapping mapping;

		public HttpMapping(HttpProcessor processor, UrlMapping mapping) {
			this.processor = processor;
			this.mapping = mapping;
		}

	}

	private class HttpRenderingMapping{

		public RenderingProcessorResolver processor;
		public UrlMapping mapping;

		public HttpRenderingMapping(RenderingProcessorResolver resolver, UrlMapping mapping) {
			this.processor = resolver;
			this.mapping = mapping;
		}

	}



}
