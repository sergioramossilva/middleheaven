package org.middleheaven.email;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataSource;



/**
 * A email message. This class wraps javax.mail and javax.activation classes
 * to facilitate email creation
 * 
 */
public class Email implements Serializable {
 
	private static final long serialVersionUID = 1L;
	
	private DataSource body;
	private String from;
    private String subject;
    private Map<EmailRecipientType,List<String>> recipients = new EnumMap<EmailRecipientType, List<String>>(EmailRecipientType.class);
    private Date sendDate;
	private Collection<DataSource> attachments = new ArrayList <DataSource>(); 
	
	/**
     * Message Sender
	 * @param senderEmailAddress
	 */ 
	public Email setFrom(String senderEmailAddress) {
        this.from = senderEmailAddress;
        return this;
	}
	 
    /**
     * Message subject
     * @param subjectEmailAddress
     */
	public Email setSubject(String subjectEmailAddress) {
        this.subject = subjectEmailAddress;
        return this;
	}
	 
    /**
     * Message body
     * @param body
     */
	public Email setBody(DataSource body) {
	    this.body = body;
	    return this;
    }
	 
    /**
     * Message body as a simple , unformatted string
     * @param body
     */
	public Email setBody(String body) {
        this.body = new PlainTextDataSource(body,"body");
        return this;
	}
	 
    /**
     * Add an attachment
     * @param atachment to be added
     */
	public Email addAtachment(DataSource atachment) {
        attachments.add(atachment);
        return this;
	}
	
    /**
     * Add a recipient address
     * @param adress
     */
    public Email addToAdress(String ... adress){
        addRecipients(EmailRecipientType.TO, adress);
        return this;
    }
    
    /**
     * Add a copy recipient address
     * @param adress
     */
    public Email addCCAdress(String ... adress){
        addRecipients(EmailRecipientType.CC, adress);
        return this;
    }
    
    /**
     * Add a blind copy recipient address
     * @param adress
     */
    public Email addBCCAdress(String ... adress){
        addRecipients(EmailRecipientType.BCC, adress);
        return this;
    }

	public void addRecipients(EmailRecipientType type, String ... addresses) {
        List<String> adresses = this.recipients.get (type);
        if (adresses==null){
            adresses = new ArrayList<String>();
        }
        
        for (String address : addresses){
            adresses.add(address);
        }

        this.recipients.put(type,adresses);
            
	}
	 
	public List<String> getRecipients(EmailRecipientType type) {
		List<String> adresses = this.recipients.get(type);
        if (adresses==null){
        	return Collections.emptyList();
        } else {
        	return Collections.unmodifiableList(adresses);
        }
	}
	 
    /**
     * 
     * @return sender address
     */
	public String getFrom() {
		return this.from;
	}
	 
    /**
     * 
     * @return message body
     */
	public DataSource getBody() {
		return this.body;
	}
	 
    /**
     * 
     * @return attachments collection
     */
	public Collection<DataSource> getAttachments() {
		return this.attachments;
	}
	 
    /**
     *  
     * @return message subject
     */
	public String getSubject() {
		return this.subject;
	}
	 
	/**
	 * The send date. If not defined return the current date
	 */
	public Date getSendDate() {
		return this.sendDate==null ? new Date(): this.sendDate;
	}
	 
    /**
     * 
     * The apparent date the email is sent. This is not the real date when the 
     * email is send, is only the date the user wants to appear as the send date.
     * @param date - email sending date
     */
	public void setSendDate(Date date) {
        this.sendDate = date; 
	}

	 
    
    
}
 
