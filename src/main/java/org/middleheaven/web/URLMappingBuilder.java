package org.middleheaven.web;

public interface URLMappingBuilder {

	
	public ActionMappingBuilder withAction(String actionName);
	
	public URLMappingBuilder with(Interceptor interceptor);

	public ActionMappingBuilder withNoAction();
}
