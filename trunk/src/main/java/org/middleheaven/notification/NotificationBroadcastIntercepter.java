package org.middleheaven.notification;

public interface NotificationBroadcastIntercepter {

	/**
	 * 
	 * @param notification
	 * @return <code>true</code> if the the next filter may be called, <code>false</code> otherwise.
	 */
	public void onIntercept (Notification notification,InterceptorChain chain);
}
