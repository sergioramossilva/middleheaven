package org.middleheaven.email.service;

import org.middleheaven.core.services.Publish;
import org.middleheaven.core.services.ServiceAtivatorContext;
import org.middleheaven.core.services.discover.ServiceActivator;

public class JavaMailActivator extends ServiceActivator {

	JavaMailEmailService service = new JavaMailEmailService();
	
	@Publish
	public EmailService getEmailService(){
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
