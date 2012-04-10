package org.middleheaven.core.wiring;

/**
 * Intercepts the wiring chain
 */
public interface WiringInterceptor {

	
	public void intercept (InterceptionContext context, InterceptorChain chain);
}
