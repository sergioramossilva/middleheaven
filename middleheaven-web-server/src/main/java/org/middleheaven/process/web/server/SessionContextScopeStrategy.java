/**
 * 
 */
package org.middleheaven.process.web.server;

import java.util.Enumeration;

import javax.servlet.http.HttpSession;

import org.middleheaven.process.ContextScope;
import org.middleheaven.util.coersion.TypeCoercing;

class SessionContextScopeStrategy extends AbstractEnumerationBasedContextScopeStrategy {

	private HttpSession httpSession;
	
	public SessionContextScopeStrategy (HttpSession httpSession){
		super(ContextScope.SESSION);
		this.httpSession= httpSession;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Enumeration<String> getEnumeration() {
		return httpSession.getAttributeNames();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object getValue(String name) {
		return httpSession.getAttribute(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T getAttribute(String name, Class<T> type) {
		return TypeCoercing.coerce(httpSession.getAttribute(name), type);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAttribute(String name, Object value) {
		this.httpSession.setAttribute(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAttribute(String name) {
		this.httpSession.removeAttribute(name);
	}
	
}