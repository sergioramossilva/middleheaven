package org.middleheaven.notification;

public interface NotificationService {

	
	public void addListener (NotificationListener listener, NotificationFilter filter);
	public void removeListener (NotificationListener listener);
	public void broadcast(Notification notification);
	public void addAdpater(BroadcastAdapter adapter);
}
