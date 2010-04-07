package org.middleheaven.mail.service;

import org.middleheaven.core.wiring.activation.ActivationContext;
import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;
import org.middleheaven.core.wiring.annotations.Wire;
import org.middleheaven.mail.MailSendingService;
import org.middleheaven.namedirectory.NameDirectoryService;

public class JavaMailActivator extends Activator {

	MailSendingService service;
	private NameDirectoryService nameService ;
	
	@Publish
	public MailSendingService getEmailService(){
		return service;
	}
	
	@Wire(required=false)
	public void setNameDirectoryService(NameDirectoryService nameService){
		this.nameService = nameService;
	}
	
	@Override
	public void activate(ActivationContext context) {
		
		if(nameService!=null){
			this.service = new NameDirectoryMailSessionSendingService(nameService);
		} else {
			this.service = new LocalMailSendingService();
		}
	}

	@Override
	public void inactivate(ActivationContext context) {
		// no-op

	}

}
