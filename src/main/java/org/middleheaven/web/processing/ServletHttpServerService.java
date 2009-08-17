package org.middleheaven.web.processing;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.middleheaven.aas.old.AccessDeniedException;
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
	public ControlProcessor resolveControlProcessor(String url) {
		for (HttpMapping mapping : processorsMappings.values()){
			if (mapping.mapping.match(url)){
				return mapping.processor;
			}
		}
		return null;
	}

	@Override
	public void registerHttpProcessor(String processorID, ControlProcessor processor,UrlMapping mapping) {
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
			response.sendError(HttpError.NOT_FOUND.errorCode()); 
			return;
		}

		if (!available){
			response.sendError(HttpError.SERVICE_UNAVAILABLE.errorCode()); 
			return;
		}

		ControlProcessor processor = resolveControlProcessor(request.getRequestURI());

		if (processor == null){
			response.sendError(HttpError.NOT_IMPLEMENTED.errorCode()); 
			return;
		}


		// TODO apply filter

		try{
			RequestResponseWebContext context = new  RequestResponseWebContext(request,response);
			Outcome outcome = processor.process(context);

			if(outcome ==null){
				response.sendError(HttpError.INTERNAL_SERVER_ERROR.errorCode());
			} else if (outcome.isTerminal()){
				return; // do nothing. The response is already done
			} else if (outcome.isError){
				response.sendError(Integer.parseInt(outcome.getUrl()));
			}else if (outcome.isDoRedirect()){
				response.sendRedirect(outcome.getUrl());
			} else {
				RenderingProcessor render = this.resolverRenderingProcessor(outcome.getUrl());

				render.process(context, outcome);
			}

		}catch (AccessDeniedException e){
			response.sendError(HttpError.FORBIDDEN.errorCode());
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

		public ControlProcessor processor;
		public UrlMapping mapping;

		public HttpMapping(ControlProcessor processor, UrlMapping mapping) {
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
