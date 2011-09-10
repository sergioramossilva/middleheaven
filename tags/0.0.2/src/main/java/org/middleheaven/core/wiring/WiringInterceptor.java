package org.middleheaven.core.wiring;

/**
 * Intercepts the wiring chain
 */
public interface WiringInterceptor {

	
	public  <T> void intercept (InterceptionContext<T> context, InterceptorChain<T> chain);
}
