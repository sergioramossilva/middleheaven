package org.middleheaven.mail;


public interface MailAsynchrounsCallbak {

	/**
	 * Called when after the email is sent sucessufly or
	 * the send is not sucesfull
	 */
	public void onSent (MailMessage email, boolean sucess);
}
