package org.middleheaven.mail;

import org.middleheaven.core.annotations.Service;


@Service
public interface MailSendingService {

	/**
	 * Tries to send the mail message synchronously 
	 * @param message
	 * @throws MailException
	 */
	void send(MailMessage message) throws MailException;
	
	/**
	 * Tries to send the email asynchronously. 
	 * Because may there might be problems wile sending the email
	 * the sender can be informed of success/failure when the real transport is performed.
	 * 
	 * @param message the email to send
	 * @param callback the callbak to warn after the email is sent
	 */
	void send (MailMessage message, MailAsynchrounsCallback callback ) throws MailException;

}
