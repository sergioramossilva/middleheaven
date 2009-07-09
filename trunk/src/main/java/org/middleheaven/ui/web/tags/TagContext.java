package org.middleheaven.ui.web.tags;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.middleheaven.ui.ContextScope;
import org.middleheaven.web.processing.action.ServletWebContext;


public class TagContext extends ServletWebContext {

	PageContext pageContex;
	public TagContext(PageContext pageContext) {
		this.pageContex = pageContext;
	}

	@Override
	public Map<String, String> getParameters() {
		return Collections.emptyMap();
	}
	
	@Override
	protected ServletRequest getRequest() {
		return pageContex.getRequest();
	}

	@Override
	protected ServletContext getServletContext() {
		return pageContex.getServletContext();
	}

	@Override
	protected HttpSession getSession() {
		return pageContex.getSession();
	}

	@Override
	protected void setHeaderAttribute(ContextScope scope, String name, Object value) {
		throw new UnsupportedOperationException("Cannot set " + scope.toString() + " in the " + this.getClass().getSimpleName());
	}

	@Override
	protected ServletResponse getResponse() {
		return pageContex.getResponse();
	}

	@Override
	public InetAddress getRemoteAddress() {
		try{
			return InetAddress.getByName(pageContex.getRequest().getRemoteAddr());
		}catch (UnknownHostException e){
			return null;
		}
	}


}
