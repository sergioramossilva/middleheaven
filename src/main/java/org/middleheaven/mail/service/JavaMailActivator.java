package org.middleheaven.mail.service;

import org.middleheaven.core.services.ServiceAtivatorContext;
import org.middleheaven.core.services.discover.ServiceActivator;
import org.middleheaven.core.wiring.activation.Publish;
import org.middleheaven.mail.MailSendingService;

public class JavaMailActivator extends ServiceActivator {

	JavaMailSendingService service = new JavaMailSendingService();
	
	@Publish
	public MailSendingService getEmailService(){
		return service;
	}
	
	@Override
	public void activate(ServiceAtivatorContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void inactivate(ServiceAtivatorContext context) {
		// TODO Auto-generated method stub

	}

}
