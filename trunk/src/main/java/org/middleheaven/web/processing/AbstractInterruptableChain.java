package org.middleheaven.web.processing;

import java.util.ArrayList;
import java.util.List;

import org.middleheaven.logging.Logging;
import org.middleheaven.ui.ContextScope;
import org.middleheaven.web.processing.action.BasicOutcomeStatus;
import org.middleheaven.web.processing.action.OutcomeStatus;

public abstract class AbstractInterruptableChain<F> {

	private final List <Chainnable> filters;
	private int current=0;
	private Outcome outcome;
	private boolean interrupted = false;
	private boolean endIsReached = false;
	
	public AbstractInterruptableChain (List <F> filters ,  Outcome defaultOutcome){
		this.filters = new ArrayList<Chainnable>();
		for (F f : filters){
			this.filters.add(new ChainnableAdapter(f));
		}
		this.filters.add(new Chainnable(){

			@Override
			public void doInChain(HttpContext context, AbstractInterruptableChain chain) {
				interruptWithOutcome(doFinal(context));
				endIsReached = true;
			}
			
		});
		
		this.outcome = defaultOutcome;
	}
	
	public Outcome getOutcome() {
		return outcome;
	}

	private interface Chainnable {
		void doInChain(HttpContext context, AbstractInterruptableChain chain);
	}
	
	private class ChainnableAdapter implements Chainnable{
		
		private F obj;

		public ChainnableAdapter(F obj) {
			this.obj = obj;
		}

		@Override
		public void doInChain(HttpContext context, AbstractInterruptableChain chain) {
			 call(obj, context, chain);
		}
	}
	

	private Outcome resolveOutcome (OutcomeStatus status){
		return new Outcome(status,HttpCode.SERVICE_UNAVAILABLE);
	}
	
	public void doChain(HttpContext context) throws HttpProcessException{
		if (current<filters.size()){
			current++;
			if(interrupted || endIsReached){
				return;
			}
			
			final Chainnable chainnable = filters.get(current-1);
			try {
				// invoque 
				
				chainnable.doInChain(context, this);
				
			} catch (Exception e){
				Logging.error("Exception found handling " + chainnable.getClass(), e);
				context.setAttribute(ContextScope.REQUEST, "exception", e);
				this.interruptWithOutcome(resolveOutcome(BasicOutcomeStatus.FAILURE));
			} catch (Error e){
				Logging.fatal("Exception found handling interceptor " + chainnable.getClass(), e);
				context.setAttribute(ContextScope.REQUEST, "exception", e);
				Outcome outcome =  resolveOutcome(BasicOutcomeStatus.ERROR);
				if (outcome==null){
					outcome =  resolveOutcome(BasicOutcomeStatus.FAILURE);
				}
				this.interruptWithOutcome(outcome);
			}
			
		} 
	}
	
	public final void interruptWithOutcome(Outcome outcome){
		interrupted  = true;
		this.outcome = outcome;
	}

	protected abstract Outcome doFinal(HttpContext context);
	protected abstract void call(F element, HttpContext context, AbstractInterruptableChain chain);
	

	
}
