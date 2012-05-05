package org.middleheaven.ui.rendering;

import org.middleheaven.process.AttributeContext;
import org.middleheaven.process.ContextScope;
import org.middleheaven.process.MapContext;
import org.middleheaven.process.ScopedAttributesResolutionStrategy;

public class RenderingContext implements AttributeContext{

	public RenderingContext(RenderKit kit) {
		this(new MapContext(), kit);
	}
	
	AttributeContext context;
	RenderKit kit;
	public RenderingContext(AttributeContext context, RenderKit kit) {
		this.context = context;
		this.kit = kit;
	}
	
	public RenderKit getRenderKit(){
		return kit;
	}

	@Override
	public <T> T getAttribute(ContextScope scope, String name, Class<T> type) {
		return context.getAttribute(scope,name, type);
	}

	@Override
	public <T> T getAttribute(String name, Class<T> type) {
		return context.getAttribute(name, type);
	}

	@Override
	public void setAttribute(ContextScope scope, String name, Object value) {
		context.setAttribute(scope,name,value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScopedAttributesResolutionStrategy getScopeAttributeContext(ContextScope scope) {
		return context.getScopeAttributeContext(scope);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAttribute(ContextScope scope, String name) {
		context.removeAttribute(scope, name);
	}


}
