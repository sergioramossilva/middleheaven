/**
 * 
 */
package org.middleheaven.process.web.server;

import java.util.Enumeration;

import javax.servlet.http.HttpSession;

import org.middleheaven.process.ContextScope;

class ServletSessionContextScopeStrategy extends AbstractEnumerationBasedScopedAttributesResolutionStrategy {

	private HttpSession httpSession;
	
	public ServletSessionContextScopeStrategy (HttpSession httpSession){
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