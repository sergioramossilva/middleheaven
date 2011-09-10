package org.middleheaven.process.web.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import org.middleheaven.global.Culture;
import org.middleheaven.io.repository.BufferedMediaManagedFileContent;
import org.middleheaven.io.repository.EmptyFileRepository;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.MediaManagedFile;
import org.middleheaven.process.ContextScope;
import org.middleheaven.process.MapContext;
import org.middleheaven.process.ScopeAttributeContext;
import org.middleheaven.process.web.HttpMethod;
import org.middleheaven.process.web.HttpUrl;
import org.middleheaven.process.web.HttpUserAgent;
import org.middleheaven.process.web.server.global.HttpCultureResolver;
import org.middleheaven.process.web.server.global.RequestAgentHttpCultureResolver;


public final class MapWebContext  extends WebContext{

	private HttpUserAgent agent;
	private HttpMethod service;
	private MapContext mapContext;
	private Culture culture= Culture.defaultValue();
	private InetAddress address;
	private String url;
	private String contextPath;
	
	public MapWebContext(String url , String contextPath, HttpMethod service) {
		this(url,contextPath, service, new RequestAgentHttpCultureResolver());
	}
	
	public MapWebContext(String url , String contextPath, HttpMethod service, HttpCultureResolver httpCultureResolveService) {
		this.service = service;
		this.url = url;
		this.contextPath = contextPath;
		this.mapContext = new MapContext();
		try {
			this.address = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			this.address = null;
		}

	}

	@Override
	public InetAddress getRemoteAddress() {
		return address;
	}
	
	public void setRemote(InetAddress address){
		this.address = address;
	}
	
	public void setAgent(HttpUserAgent agent){
		this.agent = agent;
	}
	
	
	public void setCulture(Culture culture) {
		this.culture = culture;
	}

	@Override
	public String getContextPath() {
		return this.contextPath;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpServerRequest getRequest() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpServerResponse getResponse() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFileRepository getUploadRepository() {
		// TODO Auto-generated method stub
		return null;
	}






}
