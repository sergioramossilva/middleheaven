/**
 * 
 */
package org.middleheaven.process.web.server;

import java.util.Enumeration;

import javax.servlet.ServletContext;

import org.middleheaven.process.ContextScope;
import org.middleheaven.util.coersion.TypeCoercing;

class InitConfigurationContextScopeStrategy extends AbstractEnumerationBasedContextScopeStrategy {

	private ServletContext context;
	
	public InitConfigurationContextScopeStrategy (ServletContext context){
		super(ContextScope.CONFIGURATION);
		this.context= context;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Enumeration<String> getEnumeration() {
		return context.getInitParameterNames();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object getValue(String name) {
		return context.getInitParameter(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T getAttribute(String name, Class<T> type) {
		return TypeCoercing.coerce(context.getInitParameter(name), type);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAttribute(String name, Object value) {
		throw new UnsupportedOperationException("Is not possible to change the attribute in " + this.getScope());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAttribute(String name) {
		throw new UnsupportedOperationException("Is not possible to remove an attribute from " + this.getScope());
	}
	
}