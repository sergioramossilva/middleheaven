package org.middleheaven.ui.rendering;

import java.util.Enumeration;

import org.middleheaven.global.Culture;
import org.middleheaven.ui.Context;
import org.middleheaven.ui.ContextScope;
import org.middleheaven.ui.MapContext;

public class RenderingContext implements Context{

	public RenderingContext(RenderKit kit) {
		this(new MapContext(null), kit);
	}
	
	Context context;
	RenderKit kit;
	public RenderingContext(Context context, RenderKit kit) {
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

	@Override
	public Culture getCulture() {
		return context.getCulture();
	}

}
