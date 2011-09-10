package org.middleheaven.mail.service;

import javax.mail.Session;

import org.middleheaven.mail.MailException;
import org.middleheaven.mail.MailSendingService;
import org.middleheaven.namedirectory.NameDirectoryService;
import org.middleheaven.namedirectory.NamingDirectoryException;

public class NameDirectoryMailSessionSendingService extends AbstractMailSessionSendingService implements MailSendingService {

	
	private NameDirectoryService service;

	public NameDirectoryMailSessionSendingService(NameDirectoryService service){
		this.service = service;
	}
	
	@Override
	protected Session getMailSession() {
		try{
			return service.lookup("java:comp/env/mail/Session", Session.class);
		} catch (NamingDirectoryException e){
			throw new MailException(e);
		}
		
	}

}
