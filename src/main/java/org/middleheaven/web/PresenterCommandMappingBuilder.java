package org.middleheaven.web;


public class PresenterCommandMappingBuilder {

	
	public static PresenterCommandMappingBuilder map(Class<?> presenterClass){
		return new PresenterCommandMappingBuilder(presenterClass);
	}
	
	private PresenterWebCommandMapping mapping;
	private PresenterCommandMappingBuilder (Class<?> presenterClass){
		this.mapping = new PresenterWebCommandMapping(presenterClass);
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
			Outcome outcome = new Outcome(status, false, url);
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
			Outcome outcome = new Outcome(status, true, url);
			PresenterCommandMappingBuilder.this.mapping.addOutcome(outcome);
			return PresenterCommandMappingBuilder.this;
		}
		
		
	}



}
