package org.middleheaven.web;

import java.util.Enumeration;

import javax.servlet.ServletContext;

import org.middleheaven.util.conversion.TypeConvertions;

public class ConfigContext implements Context {

	ServletContext ctx;
	
	public ConfigContext(ServletContext ctx) {
		this.ctx = ctx;
	}

	
	@Override
	public <T> T getAttribute(ContextScope scope, String name, Class<T> type) {
		Object value;
		switch (scope){
		case CONFIGURATION:
			value = ctx.getInitParameter(name);
			break;
		case APPLICATION:
			value = ctx.getAttribute(name);
			break;
		default:
			throw new IllegalArgumentException("Unavailable scope " + scope);
		}

		if ( value == null){
			return null;
		}
		return TypeConvertions.convert(value, type);

	}

	@Override
	public void setAttribute(ContextScope scope, String name, Object value) {
		
		switch (scope){
		case APPLICATION:
			ctx.setAttribute(name,value);
			break;
		case CONFIGURATION:
			throw new IllegalArgumentException(scope + " is read-only");
		default:
			throw new IllegalArgumentException("Unavailable scope " + scope);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Enumeration<String> getAttributeNames(ContextScope scope) {
		switch (scope){
		case CONFIGURATION:
			return ctx.getInitParameterNames();
		case APPLICATION:
			return ctx.getAttributeNames();
		default:
			throw new IllegalArgumentException("Unavailable scope " + scope);
		}
	}

	@Override
	public <T> T getAttribute(String name, Class<T> type) {
		ContextScope[] scopes = new ContextScope[]{
			ContextScope.APPLICATION,
			ContextScope.CONFIGURATION};
		for (ContextScope scope : scopes){
			T obj = this.getAttribute(scope, name, type);
			if (obj!=null){
				return obj;
			}
		}
		return null;
	}
	
}
