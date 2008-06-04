package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

public class InterceptorResolver<T> implements Resolver<T> {

	
	private final Resolver<T> original;
	private final List<Interceptor> interceptors;
	
	public InterceptorResolver(List<Interceptor> interceptors,Resolver<T> original) {
		super();
		this.original = original;
		this.interceptors = interceptors;
	}

	@Override
	public T resolve(Class<T> type, Set<Annotation> specificationsSet) {

		InterceptorChain<T> chain = new InterceptorChain<T>(interceptors,original);
		InterceptionContext<T> context = new InterceptionContext<T>(type,specificationsSet);

		chain.doChain(context);

		return context.getObject();

	}





}
