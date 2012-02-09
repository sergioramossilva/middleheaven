package org.middleheaven.mail;

public class MailTransmissionResult {

	private MailMessage mail;
	private Exception exception;
	
	public MailTransmissionResult(MailMessage mail, Exception exception) {
		super();
		this.mail = mail;
		this.exception = exception;
	}

	public MailMessage getMail() {
		return mail;
	}

	public boolean isSucess() {
		return  exception == null;
	}

	public Exception getException() {
		return exception;
	}


	
}
