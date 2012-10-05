/**
 * 
 */
package org.middleheaven.process.web.server;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.middleheaven.process.Attribute;
import org.middleheaven.process.ContextScope;
import org.middleheaven.process.ScopedAttributesResolutionStrategy;
import org.middleheaven.process.web.HttpCookie;
import org.middleheaven.process.web.server.action.ServletCookieBagTranslator;

class CookiesScopedAttributesResolutionStrategy implements ScopedAttributesResolutionStrategy {

	private ServletCookieBagTranslator translator;

	public CookiesScopedAttributesResolutionStrategy (HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		this.translator = new ServletCookieBagTranslator(((HttpServletRequest) httpRequest) , ((HttpServletResponse) httpResponse));
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
		return false; // this is read-only
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
		return translator.getSize();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return translator.getSize() == 0;
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
			return type.cast(translator.readAll().toArray());
		} else if (type.isAssignableFrom(HttpCookie.class)){
			return type.cast(translator.readAll().getCookie(name));
		} else {
			throw new IllegalArgumentException("Illegal type for scope " + this.getScope());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAttribute(String name, Object value) {
		if (value instanceof HttpCookie) {
			translator.write( ((HttpCookie) value));
		}
		
	
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAttribute(String name) {
		throw new UnsupportedOperationException("Request cookies are read only");
	}

	
	
}