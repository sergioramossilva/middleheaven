package org.middleheaven.notification;


import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;

public class NotificationServiceActivator extends Activator {

	NotificationService service = new MapNotificationService();
	
	@Publish
	public NotificationService getNotificationService(){
		return service;
	}
	
	@Override
	public void activate() {

	}

	@Override
	public void inactivate() {
		
	}

	
	
	
}
