package org.middleheaven.web.processing;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.middleheaven.logging.Logging;
import org.middleheaven.web.processing.action.HttpProcessIOException;
import org.middleheaven.web.processing.action.HttpProcessServletException;
import org.middleheaven.web.processing.action.RequestResponseWebContext;

// created directly on the WebContainerBoostrap
class ServletHttpServerService implements HttpServerService {

	private final Map<String, HttpMapping> mappings = new TreeMap<String, HttpMapping>();

	private boolean available = false;
	private boolean stopped = false;


	public ServletHttpServerService(){

	}

	@Override
	public HttpProcessor processorFor(String url) {
		for (HttpMapping mapping : mappings.values()){
			if (mapping.mapping.match(url)){
				return mapping.processor;
			}
		}
		return null;
	}

	@Override
	public void register(String processorID, HttpProcessor processor,UrlMapping mapping) {
		mappings.put(processorID, new HttpMapping(processor,mapping));

	}

	@Override
	public void unRegister(String processorID) {
		mappings.remove(processorID);
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
			response.sendError(HttpErrors.NOT_FOUND.errorCode()); 
			return;
		}

		if (!available){
			response.sendError(HttpErrors.SERVICE_UNAVAILABLE.errorCode()); 
			return;
		}

		HttpProcessor processor = processorFor(request.getRequestURI());

		if (processor == null){
			response.sendError(HttpErrors.NOT_IMPLEMENTED.errorCode()); 
			return;
		}
		
		try{
			Outcome outcome = processor.process(new  RequestResponseWebContext(request,response));
			
			if (outcome.isTerminal()){
				return; // do nothing. The response is already done
			} else if (outcome.isError){
				response.sendError(Integer.parseInt(outcome.getUrl()));
			}else if (outcome.isDoRedirect()){
				response.sendRedirect(outcome.getUrl());
			} else {
				request.getRequestDispatcher(outcome.getUrl()).include(request, response);
			}
			
		}catch (HttpProcessIOException e){
			throw e.getIOException();
		}catch (HttpProcessServletException e){
			throw e.getServletException();
		}catch (Throwable e){
			throw new ServletException(e);
		} 
	}

	private class HttpMapping{

		public HttpProcessor processor;
		public UrlMapping mapping;

		public HttpMapping(HttpProcessor processor, UrlMapping mapping) {
			this.processor = processor;
			this.mapping = mapping;
		}

	}
}
