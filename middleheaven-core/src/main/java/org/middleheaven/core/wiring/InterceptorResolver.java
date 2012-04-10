package org.middleheaven.core.wiring;

import java.util.List;

/**
 * Allows for interception of the resolve request.
 *
 * @param <T>
 */
public class InterceptorResolver implements Resolver {

	
	private final Resolver  original;
	private final List<WiringInterceptor> interceptors;

	public InterceptorResolver(List<WiringInterceptor> interceptors,Resolver original) {
		super();
		this.original = original;
		this.interceptors = interceptors;
	}

	@Override
	public Object resolve(ResolutionContext context, WiringQuery query) {

		InterceptorChain chain = new InterceptorChain(interceptors,original);
		InterceptionContext interceptionContext = new InterceptionContext(context, query);

		chain.doChain(interceptionContext);

		return interceptionContext.getObject();

	}





}
