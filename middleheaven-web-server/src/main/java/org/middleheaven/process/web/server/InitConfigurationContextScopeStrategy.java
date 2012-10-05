/**
 * 
 */
package org.middleheaven.process.web.server;

import java.util.Enumeration;

import javax.servlet.ServletContext;

import org.middleheaven.process.ContextScope;

class ServletInitConfigurationScopedAttributesResolutionStrategy extends AbstractEnumerationBasedScopedAttributesResolutionStrategy {

	private ServletContext context;
	
	public ServletInitConfigurationScopedAttributesResolutionStrategy (ServletContext context){
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