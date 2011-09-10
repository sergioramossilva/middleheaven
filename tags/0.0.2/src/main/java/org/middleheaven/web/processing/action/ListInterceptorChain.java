package org.middleheaven.web.processing.action;

import java.util.List;

import org.middleheaven.web.processing.AbstractInterruptableChain;
import org.middleheaven.web.processing.HttpContext;
import org.middleheaven.web.processing.Outcome;

public class ListInterceptorChain extends AbstractInterruptableChain<Interceptor> implements InterceptorChain{


	public ListInterceptorChain(List<Interceptor> interceptors){
		super(interceptors, new TerminalOutcome());
	}
	

	protected Outcome doFinal(HttpContext context){ 
		return this.getOutcome();
	}

	@Override
	protected void call(Interceptor interceptor, HttpContext context,AbstractInterruptableChain chain) {
		interceptor.intercept(context, (ListInterceptorChain)chain);
	}



}
