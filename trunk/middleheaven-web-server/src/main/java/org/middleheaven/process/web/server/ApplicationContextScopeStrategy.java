/**
 * 
 */
package org.middleheaven.process.web.server;

import java.util.Enumeration;

import javax.servlet.ServletContext;

import org.middleheaven.process.ContextScope;
import org.middleheaven.util.coersion.TypeCoercing;

/**
 * 
 */
class ApplicationContextScopeStrategy extends AbstractEnumerationBasedContextScopeStrategy {

	private ServletContext context;
	
	public ApplicationContextScopeStrategy (ServletContext context){
		super(ContextScope.APPLICATION);
		this.context= context;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Enumeration<String> getEnumeration() {
		return context.getAttributeNames();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object getValue(String name) {
		return context.getAttribute(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T getAttribute(String name, Class<T> type) {
		return TypeCoercing.coerce(context.getAttribute(name), type);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAttribute(String name, Object value) {
		context.setAttribute(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAttribute(String name) {
		context.removeAttribute(name);
	}
	
}