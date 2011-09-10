package org.middleheaven.core.wiring;

import java.util.List;

/**
 * Allows for interception of the resolve request.
 *
 * @param <T>
 */
public class InterceptorResolver<T> implements Resolver<T> {

	
	private final Resolver<T> original;
	private final List<WiringInterceptor> interceptors;
	
	public InterceptorResolver(List<WiringInterceptor> interceptors,Resolver<T> original) {
		super();
		this.original = original;
		this.interceptors = interceptors;
	}

	@Override
	public T resolve(WiringSpecification<T> query) {

		InterceptorChain<T> chain = new InterceptorChain<T>(interceptors,original);
		InterceptionContext<T> context = new InterceptionContext<T>(query);

		chain.doChain(context);

		return context.getObject();

	}





}
