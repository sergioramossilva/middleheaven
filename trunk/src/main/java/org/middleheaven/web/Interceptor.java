package org.middleheaven.web;

public interface Interceptor {

	public void interceptForward (Context context);
	public void interceptReverse (Context context);
	
}
