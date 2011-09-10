package org.middleheaven.notification;

public interface NotificationBroadcaster {

	
	public void broadcast(Notification notification);
	public void addFilter(NotificationBroadcastIntercepter filter);
	public void removeFilter(NotificationBroadcastIntercepter filter);
}
