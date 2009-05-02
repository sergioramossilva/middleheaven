package org.middleheaven.mail;

public class MailException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MailException (Throwable cause){
		super(cause);
	}
}
