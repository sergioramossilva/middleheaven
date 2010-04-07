package org.middleheaven.mail;

public class MailAsynchrounsCallbackDecorator implements MailAsynchrounsCallback {

	private MailAsynchrounsCallback original;

	public MailAsynchrounsCallbackDecorator(MailAsynchrounsCallback original){
		this.original = original;
	}
	
	@Override
	public void onSent(MailTransmissionResult result ) {
		this.original.onSent(result);
	}

}
