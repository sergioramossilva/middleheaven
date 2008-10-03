package org.middleheaven.web;


public class PresenterCommandMappingBuilder {

	
	public static PresenterCommandMappingBuilder map(Class<?> presenterClass){
		return new PresenterCommandMappingBuilder(presenterClass);
	}
	
	private PresenterWebCommandMapping mapping;
	private PresenterCommandMappingBuilder (Class<?> presenterClass){
		this.mapping = new PresenterWebCommandMapping(presenterClass);
		
	}
	
	public WebCommandMapping getMapping(){
		return mapping;
	}
	
	public OutcomeBuilder onSuccess (){
		return on(OutcomeStatus.SUCCESS);
	}
	
	public OutcomeBuilder onFailure (){
		return on(OutcomeStatus.FAILURE);
	}
	
	public OutcomeBuilder onInvalid (){
		return on(OutcomeStatus.INVALID);
	}
	
	public OutcomeBuilder onError (){
		return on(OutcomeStatus.ERROR);
	}
	public OutcomeBuilder on (OutcomeStatus status){
		return new MyOutcomeBuilder(status);
	}
	
	
	public PresenterWebCommandMapping build(){
		return this.mapping;
	}


	public PresenterCommandMappingBuilder with(Interceptor interceptor) {
		this.mapping.addInterceptor(interceptor);
		return this;
	}

	public PresenterCommandMappingBuilder to(String regex) {
		
		return this;
	} 

	private class MyOutcomeBuilder implements OutcomeBuilder {
		OutcomeStatus status;
		public MyOutcomeBuilder( OutcomeStatus status){
			this.status = status;
		}
		
		@Override
		public PresenterCommandMappingBuilder forwardTo(String url) {
			Outcome outcome = new Outcome(status, false, GlobalMappings.getViewBase().concat("/").concat(url));
			PresenterCommandMappingBuilder.this.mapping.addOutcome(outcome);
			return PresenterCommandMappingBuilder.this;
		}

		@Override
		public PresenterCommandMappingBuilder redirectTo(int erroCode) {
			
			Outcome outcome = new Outcome(status, erroCode);
			PresenterCommandMappingBuilder.this.mapping.addOutcome(outcome);
			return PresenterCommandMappingBuilder.this;
		}

		@Override
		public PresenterCommandMappingBuilder redirectTo(String url) {
			Outcome outcome = new Outcome(status, true, GlobalMappings.getViewBase().concat("/").concat(url));
			PresenterCommandMappingBuilder.this.mapping.addOutcome(outcome);
			return PresenterCommandMappingBuilder.this;
		}
		
		
	}



}
