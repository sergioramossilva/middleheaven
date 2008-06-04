package org.middleheaven.core.wiring;

public interface Interceptor {

	
	public  <T> void intercept (InterceptionContext<T> context, InterceptorChain<T> chain);
}
