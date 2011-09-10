/**
 * 
 */
package org.middleheaven.process.web.server;

import java.util.Collections;
import java.util.Enumeration;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.middleheaven.process.ContextScope;

class RequestHeadersContextScopeStrategy extends AbstractEnumerationBasedContextScopeStrategy {

	private ServletRequest httpRequest;
	
	public RequestHeadersContextScopeStrategy (ServletRequest httpRequest){
		super(ContextScope.REQUEST_HEADERS);
		this.httpRequest= httpRequest;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Enumeration<String> getEnumeration() {
		if (httpRequest instanceof HttpServletRequest){
			return((HttpServletRequest) httpRequest).getHeaderNames();
		} 
		// TODO log different request type
		return Collections.<String>enumeration(Collections.<String>emptySet());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object getValue(String name) {
		if (httpRequest instanceof HttpServletRequest){
			return((HttpServletRequest) httpRequest).getHeader(name);
		} 
		// TODO log different request type
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T getAttribute(String name, Class<T> type) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAttribute(String name, Object value) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAttribute(String name) {
		// TODO Auto-generated method stub
		
	}
	
}