package org.middleheaven.process.web;


public class HttpProcessException extends RuntimeException {


	private static final long serialVersionUID = 1L;

	public HttpProcessException(Throwable cause) {
		super(cause);
	}
	
	public HttpProcessException(String message) {
		super(message);
	}

}
