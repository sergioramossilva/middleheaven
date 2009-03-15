package org.middleheaven.web;


public interface ActionMappingBuilder {

	public OutcomeBuilder onSuccess ();
	
	public OutcomeBuilder onFailure ();
	
	public OutcomeBuilder onInvalid ();
	
	public OutcomeBuilder onError ();
	
	public OutcomeBuilder on (OutcomeStatus status);

	public String getActionMame();
	
	public ActionMappingBuilder withAction(String actionName);
	
}
