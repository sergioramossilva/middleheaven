package org.middleheaven.mail.service;

import org.middleheaven.core.wiring.activation.ActivationContext;
import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;
import org.middleheaven.mail.MailSendingService;

public class JavaMailActivator extends Activator {

	JavaMailSendingService service = new JavaMailSendingService();
	
	@Publish
	public MailSendingService getEmailService(){
		return service;
	}
	
	@Override
	public void activate(ActivationContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void inactivate(ActivationContext context) {
		// TODO Auto-generated method stub

	}

}
