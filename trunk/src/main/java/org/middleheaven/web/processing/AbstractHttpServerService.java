package org.middleheaven.web.processing;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractHttpServerService implements HttpServerService {

	private final List<HttpFilter> filters = new CopyOnWriteArrayList<HttpFilter>();
	private final Map<String, HttpMapping> processorsMappings = new TreeMap<String, HttpMapping>();
	private final Map<String, HttpRenderingMapping> renderingMappings = new TreeMap<String, HttpRenderingMapping>();

	private boolean available = false;
	private boolean stopped = false;
	
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
	public RenderingProcessor resolverRenderingProcessor(String url) {
		for (HttpRenderingMapping mapping : renderingMappings.values()){
			if (mapping.mapping.match(url)){
				return mapping.processor.resolve(url);
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
	
	protected Outcome doService(HttpContext context ,HttpProcessor processor) throws HttpProcessException{
		
		final FilterChain filterChain = new FilterChain(filters,processor);
		filterChain.doChain(context);
		
		return filterChain.getOutcome();
	}

	protected abstract Outcome doOnChainEnd(HttpContext context,HttpProcessor processor , Outcome outcome) throws HttpProcessException;
	
	private  class FilterChain extends AbstractInterruptableChain<HttpFilter> implements HttpFilterChain {
		
		HttpProcessor processor;

		public FilterChain(List<HttpFilter> filters, HttpProcessor processor ){
			super (filters, null);
			this.processor = processor; 
		}
		
//		public void doChain(HttpContext context) throws HttpProcessException {
//			if (outcome==null && current<filters.size()){
//				current++;
//				filters.get(current-1).doFilter(context, this);
//			}
//			
//			if (!endIsReached && (outcome!=null || current <= filters.size()))  {
//				
//				doOnChainEnd(context, this.processor,outcome);
//				endIsReached = true;
//			}
//		}


		@Override
		protected void call(HttpFilter element, HttpContext context, AbstractInterruptableChain chain) {
			element.doFilter(context, (FilterChain)chain);
		}

		@Override
		protected Outcome doFinal(HttpContext context) {
			return doOnChainEnd( context, processor ,  this.getOutcome());
		}
		

	}

}
