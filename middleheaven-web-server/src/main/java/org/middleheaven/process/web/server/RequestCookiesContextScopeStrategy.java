/**
 * 
 */
package org.middleheaven.process.web.server;

import java.util.Iterator;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.middleheaven.process.Attribute;
import org.middleheaven.process.ContextScope;
import org.middleheaven.process.ContextScopeStrategy;
import org.middleheaven.process.web.HttpCookie;
import org.middleheaven.process.web.server.action.ServletCookieBagTranslator;

class RequestCookiesContextScopeStrategy implements ContextScopeStrategy {

	private ServletRequest httpRequest;
	private ServletCookieBagTranslator t;
	
	public RequestCookiesContextScopeStrategy (ServletRequest httpRequest){
		this.httpRequest= httpRequest;
		
		t = new ServletCookieBagTranslator(((HttpServletRequest) httpRequest));

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Attribute> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return t.getSize();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return t.getSize() == 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContextScope getScope() {
		return ContextScope.REQUEST_COOKIES;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T getAttribute(String name, Class<T> type) {
		
		if (type.isArray() && type.getComponentType().isAssignableFrom(HttpCookie.class)){
			return type.cast(t.readAll().toArray());
		} else if (type.isAssignableFrom(HttpCookie.class)){
			return type.cast(t.readAll().getCookie(name));
		} else {
			throw new IllegalArgumentException("Illegal type for scope " + this.getScope());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAttribute(String name, Object value) {
		throw new UnsupportedOperationException("Request cookies are read only. Write cookies in the response");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAttribute(String name) {
		throw new UnsupportedOperationException("Request cookies are read only");
	}

	
	
}