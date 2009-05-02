package org.middleheaven.mail.service;

import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.MediaManagedFile;
import org.middleheaven.mail.MailMessage;
import org.middleheaven.mail.MailAsynchrounsCallbak;
import org.middleheaven.mail.MailException;
import org.middleheaven.mail.MailRecipientType;
import org.middleheaven.mail.MailSendingService;

public class JavaMailSendingService implements MailSendingService {

	boolean debug = false;
	private String stmpHost;
	private String userName;
	private String password;
	
	@Override
	public void send(MailMessage email) {
		executeEmailSend(email);
	}

	@Override
	public void sendEmailAsynchronously(MailMessage email, MailAsynchrounsCallbak callback) {
		new EmailSenderThread(email,callback).start();
	}

	public void setDebug(boolean debug){
		this.debug = debug;
	}

	public void setSMTPHost(String host){
		stmpHost = host;
	}

	public void setUsername(String username){
		this.userName = username;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	private void executeEmailSend(MailMessage email){
		try {

			//Set the host smtp address
			Properties props = new Properties();
			props.put("mail.smtp.host", stmpHost);

			// create some properties and get the default Session
			Authenticator auth = new Authenticator(){
				public PasswordAuthentication getPasswordAuthentication(){
					 return new PasswordAuthentication(userName,password);
				}
			};
			
			Session session = Session.getDefaultInstance(props,auth );
			session.setDebug(debug);

			Transport.send(transformToMimeMessage(session,email));
		} catch (MessagingException e) {
			throw new MailException(e);
		}
	}

	protected Message transformToMimeMessage(Session session, MailMessage email) throws MessagingException{

		// create a message
		MimeMessage m = new MimeMessage(session);

        // set sender address
        m.setFrom( new InternetAddress(email.getFrom()));
        //m.setFrom();
        
        // set recipients
        int i;
        List<String> recipients = email.getRecipients(MailRecipientType.TO);
        Address[] adresses = new InternetAddress[recipients.size()];
        for (i=0; i<adresses.length ; i++){
            adresses[i] = new InternetAddress(recipients.get(i));
        }
        m.setRecipients(Message.RecipientType.TO, adresses);
        
        // set copy recipients
        recipients = email.getRecipients(MailRecipientType.CC);
        adresses = new InternetAddress[recipients.size()];
        for (i=0; i<adresses.length  ; i++){
        	adresses[i] = new InternetAddress(recipients.get(i));
        }
        m.setRecipients(Message.RecipientType.CC, adresses);
        
        // set blind copy recipients
        recipients = email.getRecipients(MailRecipientType.BCC);
        adresses = new InternetAddress[recipients.size()];
        for (i=0; i<adresses.length  ; i++){
        	adresses[i] = new InternetAddress(recipients.get(i));
        }
        m.setRecipients(Message.RecipientType.BCC, adresses);
        
        // set subject
        m.setSubject(email.getSubject());
        // set date
        m.setSentDate(email.getSendDate());
        
      
        // set body
        DataSource ds = new ManagedFileDataSource(email.getBody());
        
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setDataHandler(new DataHandler(ds));
        Multipart mp = new MimeMultipart();
        mp.addBodyPart(bodyPart);

        // add attachments
        MimeBodyPart part;
        for (ManagedFile attachment : email.getAttachments().listFiles()){
                part = new MimeBodyPart();
                part.setDataHandler(new DataHandler(new ManagedFileDataSource((MediaManagedFile)attachment)));
                mp.addBodyPart(part);
        }
        // set contents    
        m.setContent(mp);
      
        // Updates the appropriate header fields of this message 
        // to be consistent with the message's contents.
        m.saveChanges();
        
        return m;

	}

	private class EmailSenderThread extends Thread {
		MailMessage email;
		MailAsynchrounsCallbak callback;
		public EmailSenderThread(MailMessage email, MailAsynchrounsCallbak callback) {
			super();
			this.email = email;
			this.callback = callback;
		}

		public void run(){
			try{
				executeEmailSend(email);
				callback.onSent(email, true);
			} catch (Exception e){
				callback.onSent(email, false);
			}
		}
	}
}
