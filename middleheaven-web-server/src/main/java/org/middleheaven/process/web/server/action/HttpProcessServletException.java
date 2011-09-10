package org.middleheaven.process.web.server.action;

import javax.servlet.ServletException;

import org.middleheaven.process.web.HttpProcessException;

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
