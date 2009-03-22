package org.middleheaven.email;


public interface EmailService {

	void sendEmail(Email email);
	
	/**
	 * Tries to send the email asynchronously. 
	 * Because may there might be problems wille sending the email
	 * the sender can be informed of sucess/failure when the real transport is performed.
	 * 
	 * @param email the email to send
	 * @param callback the callbak to warn after the email is sent
	 */
	void sendEmailAsynchronously (Email email, EmailAsynchrounsCallbak callback );

}
