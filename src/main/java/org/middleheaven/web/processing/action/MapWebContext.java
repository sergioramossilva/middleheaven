package org.middleheaven.web.processing.action;

import java.util.Enumeration;
import java.util.Map;

import org.middleheaven.global.Culture;
import org.middleheaven.io.repository.BufferedMediaVirtualFile;
import org.middleheaven.io.repository.MediaManagedFile;
import org.middleheaven.ui.ContextScope;
import org.middleheaven.ui.MapContext;
import org.middleheaven.web.processing.HttpUserAgent;


public final class MapWebContext  extends WebContext{

	private HttpUserAgent agent;
	private HttpMethod service;
	private MapContext mapContext;
	private MediaManagedFile file = new BufferedMediaVirtualFile();
	private Culture culture= Culture.defaultValue();
	
	public MapWebContext(Culture culture, HttpMethod service) {
		this.service = service;
		this.mapContext = new MapContext(culture);
	}

	@Override
	public HttpUserAgent getAgent() {
		return agent;
	}
	
	public void setAgent(HttpUserAgent agent){
		this.agent = agent;
	}

	@Override
	public MediaManagedFile responseMediaFile() {
		return file;
	}
	
	public void setMediaVirtualFile(MediaManagedFile responseFile){
		this.file = responseFile;
	}

	@Override
	protected Map<String, String> getParameters() {
		return mapContext.getScopeMap(ContextScope.PARAMETERS, String.class);
	}

	@Override
	public HttpMethod getHttpService() {
		return service;
	}

	@Override
	public <T> T getAttribute(ContextScope scope, String name, Class<T> type) {
		return mapContext.getAttribute(scope,name, type);
	}

	@Override
	public Enumeration<String> getAttributeNames(ContextScope scope) {
		return mapContext.getAttributeNames(scope);
	}

	@Override
	public void setAttribute(ContextScope scope, String name, Object value) {
		mapContext.setAttribute(scope, name, value);
	}

	@Override
	public Culture getCulture() {
		return culture;
	}

	public void setCulture(Culture culture) {
		this.culture = culture;
	}

}
