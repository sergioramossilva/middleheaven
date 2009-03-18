package org.middleheaven.web.processing.action;

import java.util.Enumeration;
import java.util.Map;

import org.middleheaven.global.Culture;
import org.middleheaven.io.repository.BufferedMediaVirtualFile;
import org.middleheaven.io.repository.MediaManagedFile;
import org.middleheaven.ui.ContextScope;
import org.middleheaven.ui.MapContext;


public final class MapWebContext  extends WebContext{

	private Agent agent;
	private HttpServices service;
	private MapContext mapContext = new MapContext();
	private MediaManagedFile file = new BufferedMediaVirtualFile();
	private Culture culture= Culture.defaultValue();
	
	public MapWebContext(HttpServices service) {
		this.service = service;
	}

	@Override
	public Agent getAgent() {
		return agent;
	}
	
	public void setAgent(Agent agent){
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
	public HttpServices getHttpService() {
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
