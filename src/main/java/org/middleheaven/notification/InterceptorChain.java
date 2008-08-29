package org.middleheaven.notification;


public interface InterceptorChain {

	public void doChain(Notification notification);
	
}
