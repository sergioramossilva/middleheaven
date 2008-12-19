package org.middleheaven.core.wiring;

import java.util.LinkedList;
import java.util.List;

public class InterceptorChain<T> {

	private List<Interceptor> interceptors = new LinkedList<Interceptor>();
	private int current=-1;
	private Resolver<T> resolver;
	
	public InterceptorChain(List<Interceptor> interceptors, Resolver<T> resolver) {
		this.interceptors.addAll(interceptors);
		this.interceptors.add(new Destination());
		this.resolver = resolver;
		
	}

	public void doChain(InterceptionContext<T> context){
		if (++current < interceptors.size()){
			interceptors.get(current).intercept(context, this);
		}
	}
	
	private class Destination implements Interceptor{

		@Override
		public void intercept(InterceptionContext context, InterceptorChain chain) {
			context.setObject(resolver.resolve(context.getWiringSpecification()));
		}
		
	}
}