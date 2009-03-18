package org.middleheaven.web.processing.action;

import java.util.ArrayList;
import java.util.List;

import org.middleheaven.aas.AccessDeniedException;
import org.middleheaven.logging.Logging;
import org.middleheaven.ui.ContextScope;

public class ListInterceptorChain implements InterceptorChain{

	private List<Interceptor> interceptors;
	private int current=0;
	private boolean interrupted = false;
	private Outcome outcome = new TerminalOutcome();
	
	public ListInterceptorChain(List<Interceptor> interceptors){
		this.interceptors = new ArrayList<Interceptor>(interceptors);
	}
	
	@Override
	public void doChain(WebContext context) {
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
	protected boolean doIntercept(Interceptor interceptor, WebContext context, InterceptorChain chain) {
		try {
			// invoque interceptor
			interceptor.intercept(context, chain);
			return false;
		} catch (AccessDeniedException e){
			outcome = new Outcome(OutcomeStatus.ERROR,501); // not implemented
		} catch (Exception e){
			Logging.error("Exception found handling interceptor " + interceptor.getClass(), e);
			context.setAttribute(ContextScope.REQUEST, "exception", e);
			outcome =  resolveOutcome(OutcomeStatus.FAILURE);
		} catch (Error e){
			Logging.fatal("Exception found handling interceptor " + interceptor.getClass(), e);
			context.setAttribute(ContextScope.REQUEST, "exception", e);
			outcome =  resolveOutcome(OutcomeStatus.ERROR);
			if (outcome==null){
				outcome =  resolveOutcome(OutcomeStatus.FAILURE);
			}
		}
		return true;
	}
	
	protected Outcome resolveOutcome (OutcomeStatus status){
		return new Outcome(status,505);
	}
	
	protected Outcome doFinal(WebContext context){ 
		return outcome;
	}
	
	@Override
	public void interruptAndRedirect(String url) {
		interrupted = true;
		this.outcome = new Outcome(OutcomeStatus.TERMINATE,true,url);
	}

	@Override
	public void interruptWithError(int errorCode) {
		interrupted = true;
		this.outcome = new Outcome(OutcomeStatus.ERROR,errorCode);
	}

	public Outcome getOutcome() {
		return outcome;
	}
	

}
