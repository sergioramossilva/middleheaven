package org.middleheaven.web.processing.action;

import java.util.ArrayList;
import java.util.List;

import org.middleheaven.logging.Logging;
import org.middleheaven.ui.ContextScope;
import org.middleheaven.web.processing.HttpCode;
import org.middleheaven.web.processing.HttpContext;
import org.middleheaven.web.processing.Outcome;

public class ListInterceptorChain implements InterceptorChain{

	private List<Interceptor> interceptors;
	private int current=0;
	private boolean interrupted = false;
	private Outcome outcome = new TerminalOutcome();
	
	public ListInterceptorChain(List<Interceptor> interceptors){
		this.interceptors = new ArrayList<Interceptor>(interceptors);
	}
	
	@Override
	public void doChain(HttpContext context) {
		if (current<interceptors.size()){
			current++;
			if(interrupted || doIntercept(interceptors.get(current-1) , context, this)){
				return;
			}
		} else {
			outcome = doFinal(context);
		}
	}

	/**
	 * 
	 * @param interceptor
	 * @param context
	 * @param chain
	 * @return true if chain must be interrupted, false otherwise 
	 */
	protected boolean doIntercept(Interceptor interceptor, HttpContext context, InterceptorChain chain) {
		try {
			// invoque interceptor
			interceptor.intercept(context, chain);
			return false;
		} catch (Exception e){
			Logging.error("Exception found handling interceptor " + interceptor.getClass(), e);
			context.setAttribute(ContextScope.REQUEST, "exception", e);
			outcome =  resolveOutcome(BasicOutcomeStatus.FAILURE);
		} catch (Error e){
			Logging.fatal("Exception found handling interceptor " + interceptor.getClass(), e);
			context.setAttribute(ContextScope.REQUEST, "exception", e);
			outcome =  resolveOutcome(BasicOutcomeStatus.ERROR);
			if (outcome==null){
				outcome =  resolveOutcome(BasicOutcomeStatus.FAILURE);
			}
		}
		return true;
	}
	
	protected Outcome resolveOutcome (OutcomeStatus status){
		return new Outcome(status,HttpCode.NOT_IMPLEMENTED);
	}
	
	protected Outcome doFinal(HttpContext context){ 
		return outcome;
	}

	public Outcome getOutcome() {
		return outcome;
	}

	@Override
	public void interruptWithOutcome(Outcome outcome) {
		interrupted = true;
		this.outcome = outcome;
	}
	

}
