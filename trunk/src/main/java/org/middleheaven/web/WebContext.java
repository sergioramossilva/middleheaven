package org.middleheaven.web;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.middleheaven.util.conversion.TypeConvertions;


public class WebContext implements Context {

	HttpServletRequest request;
	HttpServletResponse response;

	public WebContext(HttpServletRequest request, HttpServletResponse response) {
		super();
		this.request = request;
		this.response = response;
	}

	@Override
	public <T> T getAttribute(ContextScope scope, String name, Class<T> type) {
		Object value;
		switch (scope){
		case APPLICATION:
			value = request.getSession().getServletContext().getAttribute(name);
			break;
		case SESSION:
			value = request.getSession().getAttribute(name);
			break;
		case REQUEST:
			value = request.getAttribute(name);
			break;
		case PARAMETERS:
			value = request.getParameter(name);
			break;
		default:
			value=null;
		}

		return TypeConvertions.convert(value, type);

	}

	@Override
	public void setAttribute(ContextScope scope, String name, Object value) {
		
		switch (scope){
		case APPLICATION:
			request.getSession().getServletContext().setAttribute(name,value);
			break;
		case SESSION:
			request.getSession().setAttribute(name,value);
			break;
		case REQUEST:
			request.setAttribute(name,value);
			break;
		case PARAMETERS:
			throw new IllegalArgumentException(scope + " is read-only");
		default:
			throw new IllegalArgumentException("Unkown scope " + scope);
		}
	}

	@Override
	public Enumeration<String> getAttributeNames(ContextScope scope) {
		switch (scope){
		case APPLICATION:
			return request.getSession().getServletContext().getAttributeNames();
		case SESSION:
			return request.getSession().getAttributeNames();
		case REQUEST:
			return request.getAttributeNames();
		case PARAMETERS:
			return request.getParameterNames();
		default:
			throw new IllegalArgumentException("Unkown scope " + scope);
		}
	}

	@Override
	public <T> T getAttribute(String name, Class<T> type) {
		ContextScope[] scopes = new ContextScope[]{
			ContextScope.PARAMETERS,
			ContextScope.REQUEST,
			ContextScope.SESSION,
			ContextScope.APPLICATION};
		for (ContextScope scope : scopes){
			T obj = this.getAttribute(scope, name, type);
			if (obj!=null){
				return obj;
			}
		}
		return null;
	}
	
}
