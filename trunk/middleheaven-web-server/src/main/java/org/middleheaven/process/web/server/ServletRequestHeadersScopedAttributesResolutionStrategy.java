/**
 * 
 */
package org.middleheaven.process.web.server;

import java.util.Collections;
import java.util.Enumeration;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.middleheaven.process.ContextScope;

/**
 * 
 */
class ServletRequestHeadersScopedAttributesResolutionStrategy extends AbstractEnumerationBasedScopedAttributesResolutionStrategy {

	private ServletRequest httpRequest;
	
	public ServletRequestHeadersScopedAttributesResolutionStrategy (ServletRequest httpRequest){
		super(ContextScope.REQUEST_HEADERS);
		this.httpRequest= httpRequest;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isReaddable() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWritable() {
		return false; // is read-only
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Enumeration<String> getEnumeration() {
		if (httpRequest instanceof HttpServletRequest){
			return((HttpServletRequest) httpRequest).getHeaderNames();
		} 
		// TODO log different request type that HttpServletRequest
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
		// TODO log different request type that HttpServletRequest
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAttribute(String name, Object value) {
		throw new UnsupportedOperationException("Not possible to remove a Header");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAttribute(String name) {
		throw new UnsupportedOperationException("Not possible to remove a Header");
	}

	
	
}