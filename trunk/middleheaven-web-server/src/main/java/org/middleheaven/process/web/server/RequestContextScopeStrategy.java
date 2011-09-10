/**
 * 
 */
package org.middleheaven.process.web.server;

import java.util.Enumeration;

import javax.servlet.ServletRequest;

import org.middleheaven.process.ContextScope;
import org.middleheaven.util.coersion.TypeCoercing;

class RequestContextScopeStrategy extends AbstractEnumerationBasedContextScopeStrategy {

	private ServletRequest request;
	
	public RequestContextScopeStrategy (ServletRequest httpRequest){
		super(ContextScope.REQUEST);
		this.request= httpRequest;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Enumeration<String> getEnumeration() {
		return request.getAttributeNames();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object getValue(String name) {
		return request.getAttribute(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T getAttribute(String name, Class<T> type) {
		return TypeCoercing.coerce(request.getAttribute(name), type);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAttribute(String name, Object value) {
		request.setAttribute(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAttribute(String name) {
		request.removeAttribute(name);
	}
	
}