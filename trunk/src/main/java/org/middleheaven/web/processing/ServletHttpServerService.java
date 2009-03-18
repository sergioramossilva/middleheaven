package org.middleheaven.web.processing;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;

// criated directly on the WebContainerBoostrap
class ServletHttpServerService implements HTTPServerService {

	private Map<String, HttpMapping> mappings = new TreeMap<String, HttpMapping>();
	
	protected ServletContext servletContext;
	public ServletHttpServerService(ServletContext servletContext){
		this.servletContext = servletContext;
	}
	
	private class HttpMapping{

		public HttpProcessor processor;
		public UrlMapping mapping;
		
		public HttpMapping(HttpProcessor processor, UrlMapping mapping) {
			this.processor = processor;
			this.mapping = mapping;
		}
		
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

}
