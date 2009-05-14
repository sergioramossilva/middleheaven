package org.middleheaven.mail;

public class MailAsynchrounsCallbakDecorator implements MailAsynchrounsCallbak {

	private MailAsynchrounsCallbak original;

	public MailAsynchrounsCallbakDecorator(MailAsynchrounsCallbak original){
		this.original = original;
	}
	
	@Override
	public void onSent(MailMessage email, boolean sucess) {
		this.original.onSent(email, sucess);
	}

}
