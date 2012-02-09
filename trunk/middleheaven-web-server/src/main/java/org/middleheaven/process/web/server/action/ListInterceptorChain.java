package org.middleheaven.process.web.server.action;

import java.util.List;

import org.middleheaven.process.web.server.AbstractInterruptableChain;
import org.middleheaven.process.web.server.HttpServerContext;
import org.middleheaven.process.web.server.Outcome;

public class ListInterceptorChain extends AbstractInterruptableChain<ActionInterceptor> implements ActionInterceptorsChain{


	public ListInterceptorChain(List<ActionInterceptor> interceptors){
		super(interceptors, new TerminalOutcome());
	}
	

	protected Outcome doFinal(HttpServerContext context){ 
		return this.getOutcome();
	}

	@Override
	protected void call(ActionInterceptor interceptor, HttpServerContext context, AbstractInterruptableChain chain) {
		interceptor.intercept(context, (ListInterceptorChain)chain);
	}



}
