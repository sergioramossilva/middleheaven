package org.middleheaven.email;


public interface EmailAsynchrounsCallbak {

	/**
	 * Called when after the email is sent sucessufly or
	 * the send is not sucesfull
	 */
	public void onSent (Email email, boolean sucess);
}
