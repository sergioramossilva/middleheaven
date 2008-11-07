package org.middleheaven.notification;


import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.discover.ServiceActivator;

public class NotificationServiceActivator extends ServiceActivator {

	NotificationService service = new MapNotificationService();
	
	@Override
	public void activate(ServiceContext context) {

		context.register(NotificationService.class, service, null);
	}

	@Override
	public void inactivate(ServiceContext context) {
		context.unRegister(NotificationService.class, service, null);
	}

	
	
	
}
