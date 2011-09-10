/**
 * 
 */
package org.middleheaven.notification;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.middleheaven.core.wiring.service.Service;
import org.middleheaven.events.EventListenersSet;

@Service
class MapNotificationService implements NotificationService,NotificationBroadcaster{

	EventListenersSet<NotificationListener> listeners = EventListenersSet.newSet(NotificationListener.class);
	List<NotificationBroadcastIntercepter> filters = new CopyOnWriteArrayList<NotificationBroadcastIntercepter>();
	
	@Override
	public void addAdpater(BroadcastAdapter adapter) {
		adapter.adapt(this);
	}

	@Override
	public void addListener(NotificationListener listener,NotificationFilter filter) {
		listeners.addListener(new FilteredNotificationListener(listener, filter));
	}

	@Override
	public void removeListener(NotificationListener listener) {
		listeners.removeListener(new FilteredNotificationListener(listener, null));
	}

	@Override
	public void broadcast(Notification notification) {
		ArrayInterceptorChain chain = new ArrayInterceptorChain(this.filters,this.listeners);
		chain.doChain(notification);
	}

	@Override
	public void addFilter(NotificationBroadcastIntercepter filter) {
		this.filters.add(filter);
	}

	@Override
	public void removeFilter(NotificationBroadcastIntercepter filter) {
		this.filters.remove(filter);
	}
	
	static class FilteredNotificationListener implements NotificationListener{

		NotificationListener listerner;
		NotificationFilter filter;
		
		public FilteredNotificationListener(NotificationListener listener,NotificationFilter filter) {
			this.listerner = listener;
			this.filter = filter;
			if (filter==null && listener instanceof NotificationFilter){
				this.filter = (NotificationFilter)listener;
			}
		}
		
		@Override
		public void handleNotification(Notification notification) {
			if (filter==null || filter.accepHandle(notification)){
				this.listerner.handleNotification(notification);
			}
		}

		public boolean equals(Object obj){
			return obj instanceof FilteredNotificationListener && equals((FilteredNotificationListener)obj);
		}
		
		public boolean equals(FilteredNotificationListener other){
			return this.listerner.equals(other.listerner);
		}
		public int hashCode(){
			return listerner.hashCode();
		}
	}
}