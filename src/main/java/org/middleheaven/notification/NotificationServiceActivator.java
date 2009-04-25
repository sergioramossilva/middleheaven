package org.middleheaven.notification;


import org.middleheaven.core.services.ServiceAtivatorContext;
import org.middleheaven.core.services.discover.ServiceActivator;
import org.middleheaven.core.wiring.activation.Publish;

public class NotificationServiceActivator extends ServiceActivator {

	NotificationService service = new MapNotificationService();
	
	@Publish
	public NotificationService getNotificationService(){
		return service;
	}
	
	@Override
	public void activate(ServiceAtivatorContext context) {

	}

	@Override
	public void inactivate(ServiceAtivatorContext context) {
		
	}

	
	
	
}
