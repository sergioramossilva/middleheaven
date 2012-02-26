package org.middleheaven.process.web.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.middleheaven.aas.old.AccessDeniedException;
import org.middleheaven.logging.Log;
import org.middleheaven.process.web.HttpProcessException;
import org.middleheaven.process.web.HttpProcessIOException;
import org.middleheaven.process.web.HttpStatusCode;
import org.middleheaven.process.web.UrlMapping;
import org.middleheaven.process.web.server.action.HttpProcessServletException;
import org.middleheaven.process.web.server.action.RequestResponseWebContext;
import org.middleheaven.process.web.server.filters.HttpFilter;
import org.middleheaven.process.web.server.filters.HttpFilterChain;
import org.middleheaven.process.web.server.global.HttpCultureResolver;
import org.middleheaven.process.web.server.global.RequestAgentHttpCultureResolver;
import org.middleheaven.web.rendering.RenderingProcessor;
import org.middleheaven.web.rendering.RenderingProcessorResolver;

public abstract class AbstractHttpServerService implements HttpServerService {

	private final List<HttpFilter> filters = new CopyOnWriteArrayList<HttpFilter>();
	private final Map<String, HttpMapping> processorsMappings = new HashMap<String, HttpMapping>();
	private final Map<String, HttpRenderingMapping> renderingMappings = new HashMap<String, HttpRenderingMapping>();
	
	private boolean available = false;
	private boolean stopped = false;
	
	private HttpCultureResolver httpCultureResolveService = new RequestAgentHttpCultureResolver();
	
	private class HttpMapping{

		public HttpProcessor processor;
		public UrlMapping mapping;

		public HttpMapping(HttpProcessor processor, UrlMapping mapping) {
			this.processor = processor;
			this.mapping = mapping;
		}

	}
	
	@Override
	public HttpCultureResolver getHttpCultureResolver() {
		return httpCultureResolveService;
	}

	@Override
	public void setHttpCultureResolver(HttpCultureResolver httpCultureResolveService) {
		this.httpCultureResolveService = httpCultureResolveService;
		
	}
	
	private class HttpRenderingMapping{

		public RenderingProcessorResolver processor;
		public UrlMapping mapping;

		public HttpRenderingMapping(RenderingProcessorResolver resolver, UrlMapping mapping) {
			this.processor = resolver;
			this.mapping = mapping;
		}

	}
	
	@Override
	public void addFilter(HttpFilter filter) {
		filters.add(filter);
	}

	@Override
	public void removeFilter(HttpFilter filter) {
		filters.remove(filter);
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
	public void removeAllRenderingProcessors() {
		renderingMappings.clear();
	} 
	
	@Override
	public RenderingProcessor resolverRenderingProcessor(String incomingUrl, String viewName , String contentType) {
		for (HttpRenderingMapping mapping : renderingMappings.values()){
			if (mapping.mapping.match(incomingUrl) && mapping.processor.canProcess(viewName, contentType)){
				return mapping.processor.resolve(viewName, contentType);
			}
		}
		return null;
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
	
	protected boolean isStopped(){
		return this.stopped;
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
	
	/**
	 * Entry point for the FrontEndServlet to call.
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

		if (isStopped()){
			Log.onBookFor(this.getClass()).warn("HttpServerService is stopped.");
			response.sendError(HttpStatusCode.NOT_FOUND.intValue()); 
			return;
		}

		if (!isAvailable()){
			Log.onBookFor(this.getClass()).warn("HttpServerService is not available.");
			response.sendError(HttpStatusCode.SERVICE_UNAVAILABLE.intValue()); 
			return;
		}

		// determine processor
		HttpProcessor processor = resolveControlProcessor(request.getRequestURI());

		if (processor == null){
			Log.onBookFor(this.getClass()).warn("ControlProcessor has not found.");
			response.sendError(HttpStatusCode.NOT_IMPLEMENTED.intValue()); 
			return;
		}

		try{


			RequestResponseWebContext context = new  RequestResponseWebContext(request,response,this.getHttpCultureResolver());

			// execute processing
			Outcome outcome = this.doService(context, processor);

			if(outcome == null){
				Log.onBookFor(this.getClass()).warn("Outcome is null for {0}", request.getRequestURI());
				response.sendError(HttpStatusCode.NOT_FOUND.intValue());
			} else if (outcome.isTerminal()){
				Log.onBookFor(this.getClass()).debug("Outcome is terminal for {0} ", request.getRequestURI());
				return; // do not process view. The response is already written.
			} else if (outcome.isError){
				response.sendError(outcome.getHttpCode().intValue());
			}else if (outcome.isDoRedirect()){
				if (HttpStatusCode.MOVED_PERMANENTLY.equals(outcome.getHttpCode())){
					response.setStatus(HttpStatusCode.MOVED_PERMANENTLY.intValue());
					response.setHeader( "Location", addContextPath(request.getContextPath(), outcome.getParameterizedURL()));
					response.setHeader( "Connection", "close" );
				} else {
					response.sendRedirect(addContextPath(request.getContextPath(), outcome.getParameterizedURL()));
				}

			} else {

				String contentType = request.getHeader("Content-Type");

				if(outcome.getContentType()!=null){
					contentType = outcome.getContentType();
				} 

				RenderingProcessor render = this.resolverRenderingProcessor(context.getRequestUrl().getFilename(), outcome.getUrl(), contentType);

				if (render == null){
					Log.onBookFor(this.getClass()).error("Render could not be found for {0}" , outcome);
					response.sendError(HttpStatusCode.NOT_FOUND.intValue());
				} else {
					render.process(context, outcome,contentType);
				}
			}

		}catch (AccessDeniedException e){
			Log.onBookFor(this.getClass()).warn("Access denied to {0}", request.getRequestURI());
			response.sendError(HttpStatusCode.FORBIDDEN.intValue());
		}catch (HttpProcessIOException e){
			throw e.getIOException();
		}catch (HttpProcessServletException e){
			throw e.getServletException();
		}catch (Throwable e){
			throw new ServletException(e);
		} 
	}
	
	protected Outcome doService(HttpServerContext context ,HttpProcessor processor) throws HttpProcessException{
		
		final FilterChain filterChain = new FilterChain(filters,processor);
		filterChain.doChain(context);
		
		return filterChain.getOutcome();
	}


	private  class FilterChain extends AbstractInterruptableChain<HttpFilter> implements HttpFilterChain {
		
		HttpProcessor processor;

		public FilterChain(List<HttpFilter> filters, HttpProcessor processor ){
			super (filters, null);
			this.processor = processor; 
		}
		

		@Override
		protected void call(HttpFilter element, HttpServerContext context, AbstractInterruptableChain chain) {
			element.doFilter(context, (FilterChain)chain);
		}

		@Override
		protected Outcome doFinal(HttpServerContext context) {
			return doOnChainEnd( context, processor ,  this.getOutcome());
		}
		

	}
	
	protected Outcome doOnChainEnd(HttpServerContext context,HttpProcessor processor , Outcome outcome) throws HttpProcessException{

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
