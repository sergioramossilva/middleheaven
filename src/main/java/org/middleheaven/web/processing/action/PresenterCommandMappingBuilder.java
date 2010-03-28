package org.middleheaven.web.processing.action;

import org.middleheaven.web.processing.HttpCode;
import org.middleheaven.web.processing.Outcome;


public class PresenterCommandMappingBuilder {

	
	public static PresenterCommandMappingBuilder map(Class<?> presenterClass){
		return new PresenterCommandMappingBuilder(presenterClass);
	}
	
	
	private PresenterWebCommandMapping mapping;

	private PresenterCommandMappingBuilder (Class<?> presenterClass){
		this.mapping = new PresenterWebCommandMapping(presenterClass);
	}
	
	public URLMappingBuilder to(String url){
		return new MyURLMappingBuilder(url);
	}
	
	public ActionMappingBuilder action(String name){
		return new MyActionMappingBuilder(name);
	}
	
	public WebCommandMapping getMapping(){
		return mapping;
	}

	public PresenterWebCommandMapping build(){
		return this.mapping;
	}

	
	private class MyURLMappingBuilder implements URLMappingBuilder {

		String url;
		public MyURLMappingBuilder(String url) {
			this.url = url;
			mapping.addPathMatcher(url);
		}

		@Override
		public MyURLMappingBuilder with(Interceptor interceptor) {
			mapping.addInterceptor(interceptor);
			return this;
		}

		@Override
		public ActionMappingBuilder withAction(String actionName) {
			return new MyActionMappingBuilder(actionName);
		}

		@Override
		public ActionMappingBuilder withNoAction() {
			return new MyActionMappingBuilder(null);
		}

		@Override
		public URLMappingBuilder asIndex() {
			PresenterCommandMappingBuilder.this.to("")
			.withNoAction().onSuccess().redirectTo(url, RedirectOptions.permanent())
			.withAction("read").onSuccess().redirectTo(url, RedirectOptions.permanent());
			
			return this;
		}
		
	
	}
	

	private class MyActionMappingBuilder implements ActionMappingBuilder{
		
		private String action;
		
		public MyActionMappingBuilder (String action){
			this.action = action !=null ? action.toLowerCase() : null;
		}
		
		public OutcomeBuilder onSuccess (){
			return on(BasicOutcomeStatus.SUCCESS);
		}
		
		public OutcomeBuilder onFailure (){
			return on(BasicOutcomeStatus.FAILURE);
		}
		
		public OutcomeBuilder onInvalid (){
			return on(BasicOutcomeStatus.INVALID);
		}
		
		public OutcomeBuilder onError (){
			return on(BasicOutcomeStatus.ERROR);
		}
		public OutcomeBuilder on (OutcomeStatus status){
			return new MyOutcomeBuilder(this,status);
		}

		@Override
		public String getActionMame() {
			return action;
		}

		@Override
		public ActionMappingBuilder withAction(String actionName) {
			return new MyActionMappingBuilder(actionName);
		}
	}

	private class MyOutcomeBuilder implements OutcomeBuilder {
		OutcomeStatus status;
		ActionMappingBuilder actionBuilder;
		
		public MyOutcomeBuilder(ActionMappingBuilder actionBuilder, OutcomeStatus status){
			this.actionBuilder = actionBuilder;
			this.status = status;
		}
		
		@Override
		public ActionMappingBuilder forwardTo(String url, String asContentType) {
			Outcome outcome = new Outcome(status, url,asContentType);
			PresenterCommandMappingBuilder.this.mapping.addOutcome(actionBuilder.getActionMame(),outcome);
			return actionBuilder;
		}
		
		@Override
		public ActionMappingBuilder forwardTo(String url) {
			return forwardTo(url, "text/html");
		}

		@Override
		public ActionMappingBuilder redirectTo(HttpCode erroCode) {
			
			Outcome outcome = new Outcome(status, erroCode);
			PresenterCommandMappingBuilder.this.mapping.addOutcome(actionBuilder.getActionMame(),outcome);
			return actionBuilder;
		}

		@Override
		public ActionMappingBuilder redirectTo(String url) {
			return this.redirectTo(url, RedirectOptions.temporary());
		}

		@Override
		public ActionMappingBuilder redirectTo(String url, RedirectOptions options) {
			Outcome outcome = new Outcome(
					status,
					url, 
					true,
					options.isPermanent() ? HttpCode.MOVED : HttpCode.MOVED_PERMANENTLY
			);
			PresenterCommandMappingBuilder.this.mapping.addOutcome(actionBuilder.getActionMame(),outcome);
			return actionBuilder;
		}

	

	}



}
