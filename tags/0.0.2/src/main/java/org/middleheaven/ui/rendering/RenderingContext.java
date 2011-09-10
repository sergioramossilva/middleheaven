package org.middleheaven.ui.rendering;

import java.util.Enumeration;

import org.middleheaven.ui.AttributeContext;
import org.middleheaven.ui.ContextScope;
import org.middleheaven.ui.MapContext;

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
	public Enumeration<String> getAttributeNames(ContextScope scope) {
		return context.getAttributeNames(scope);
	}

	@Override
	public void setAttribute(ContextScope scope, String name, Object value) {
		context.setAttribute(scope,name,value);
	}


}
