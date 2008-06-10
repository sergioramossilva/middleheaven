package org.middleheaven.email.service;

public class EmailException extends RuntimeException {


	private static final long serialVersionUID = 1L;

	public EmailException (Throwable cause){
		super(cause);
	}
}
