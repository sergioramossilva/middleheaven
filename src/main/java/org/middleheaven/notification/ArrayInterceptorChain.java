package org.middleheaven.notification;

import java.util.List;

import org.middleheaven.events.EventListenersSet;

class ArrayInterceptorChain  implements InterceptorChain {

	NotificationBroadcastIntercepter[] interceptors;
	EventListenersSet<NotificationListener> listeners;
	int current = 0;
	public ArrayInterceptorChain(List<NotificationBroadcastIntercepter> list,EventListenersSet<NotificationListener> listeners){
		this.listeners = listeners;
		this.interceptors = new  NotificationBroadcastIntercepter[list.size()]; 
		list.toArray(this.interceptors);
	}
	
	public final void doChain(Notification notification){
		
		if (current<interceptors.length){
			current++;
			interceptors[current-1].onIntercept(notification, this);
		} else {
			doFinal(notification);
		}
	}

	public void doFinal(Notification notification) {
		listeners.broadcast().handleNotification(notification);
	}
}
