package org.middleheaven.email;

public class EmailException extends RuntimeException {


	private static final long serialVersionUID = 1L;

	public EmailException (Throwable cause){
		super(cause);
	}
}
