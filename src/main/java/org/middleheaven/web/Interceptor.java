package org.middleheaven.web;

import org.middleheaven.ui.Context;

public interface Interceptor {

	public void interceptForward (Context context);
	public void interceptReverse (Context context);
	
}
