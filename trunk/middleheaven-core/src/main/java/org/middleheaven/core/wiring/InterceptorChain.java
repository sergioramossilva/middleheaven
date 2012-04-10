package org.middleheaven.core.wiring;

import java.util.LinkedList;
import java.util.List;

public class InterceptorChain {

	private List<WiringInterceptor> interceptors = new LinkedList<WiringInterceptor>();
	private int current=-1;
	private Resolver resolver;
	
	public InterceptorChain(List<WiringInterceptor> interceptors, Resolver resolver) {
		this.interceptors.addAll(interceptors);
		this.interceptors.add(new Destination());
		this.resolver = resolver;
		
	}

	public void doChain(InterceptionContext context){
		if (++current < interceptors.size()){
			interceptors.get(current).intercept(context, this);
		}
	}
	
	private class Destination implements WiringInterceptor{

		@Override
		public void intercept(InterceptionContext context, InterceptorChain chain) {
			context.setObject(resolver.resolve(context.relContext, context.getWiringQuery()));
		}
		
	}
}
