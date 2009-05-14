package org.middleheaven.mail;

public class MailSendingServiceDecorator implements MailSendingService {

	
	private MailSendingService original;

	public MailSendingServiceDecorator(MailSendingService original){
		this.original = original;
	}
	
	protected MailSendingService getOriginal() {
		return original;
	}

	protected void setOriginal(MailSendingService original) {
		this.original = original;
	}

	@Override
	public void send(MailMessage message) throws MailException {
		original.send(message);
	}

	@Override
	public void send(MailMessage message, MailAsynchrounsCallbak callback)throws MailException {
		original.send(message,callback);
	}

}
