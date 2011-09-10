package org.middleheaven.web.processing.action;

public interface URLMappingBuilder {

	/**
	 * Maps to specified internal URL all requests coming from the context root.  
	 * @return
	 */
	public URLMappingBuilder asIndex();
	
	public ActionMappingBuilder withAction(String actionName);
	
	public URLMappingBuilder with(Interceptor interceptor);

	public ActionMappingBuilder withNoAction();
}
