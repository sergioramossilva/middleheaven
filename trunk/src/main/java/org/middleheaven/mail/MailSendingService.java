package org.middleheaven.mail;


public interface MailSendingService {

	/**
	 * Tries to send the mail message assinchrounously 
	 * @param email
	 * @throws MailException
	 */
	void send(MailMessage email) throws MailException;
	
	/**
	 * Tries to send the email asynchronously. 
	 * Because may there might be problems wille sending the email
	 * the sender can be informed of sucess/failure when the real transport is performed.
	 * 
	 * @param email the email to send
	 * @param callback the callbak to warn after the email is sent
	 */
	void sendEmailAsynchronously (MailMessage email, MailAsynchrounsCallbak callback );

}
