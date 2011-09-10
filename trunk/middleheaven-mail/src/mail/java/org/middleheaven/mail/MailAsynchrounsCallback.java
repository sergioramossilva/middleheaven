package org.middleheaven.mail;


public interface MailAsynchrounsCallback {

	/**
	 * Called when after the email is sent sucessufly or
	 * the send is not sucesfull
	 */
	public void onSent (MailTransmissionResult result);
}
