package org.middleheaven.process.web.server.action;


public interface URLMappingBuilder {

	/**
	 * Maps to specified internal URL all requests coming from the context root.  
	 * @return
	 */
	public URLMappingBuilder asIndex();
	
	public ActionMappingBuilder withAction(String actionName);
	
	/**
	 * Makes the request passing through the given {@link ActionInterceptor}.
	 * 
	 * Interceptors are stacked in a call chain and called in the order they are added.
	 * 
	 * @param interceptor the interceptor to add to the interceptor chain
	 * @return this {@link URLMappingBuilder} object.
	 */
	public URLMappingBuilder through(ActionInterceptor interceptor);

	public ActionMappingBuilder withNoAction();
}
