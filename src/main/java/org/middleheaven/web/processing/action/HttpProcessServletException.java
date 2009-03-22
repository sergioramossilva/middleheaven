package org.middleheaven.web.processing.action;

import javax.servlet.ServletException;

import org.middleheaven.web.processing.HttpProcessException;

public class HttpProcessServletException extends HttpProcessException {

	private ServletException cause;

	public HttpProcessServletException(ServletException cause) {
		super(cause);
		this.cause = cause;
	}

	
	public ServletException getServletException(){
		return cause;
	}
}
