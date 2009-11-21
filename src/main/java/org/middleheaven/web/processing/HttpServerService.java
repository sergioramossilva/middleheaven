package org.middleheaven.web.processing;

/**
 * Service to process server-side HTTP requests
 *
 */
public interface HttpServerService {

	public void addFilter (HttpFilter filter);
	public void removeFilter (HttpFilter filter);
	
	
	public void registerHttpProcessor(String processorID, HttpProcessor procesor, UrlMapping mapping);
	public void unRegisterHttpProcessor(String processorID);
	
	public void addRenderingProcessorResolver(String resolverID, RenderingProcessorResolver resolver, UrlMapping mapping);
	public void removeRenderingProcessorResolver(String resolverID);
	
	/**
	 * Discovers and returns the ViewProcessor that can, according to it's registred UrlMapping, render the given url 
	 * @param url
	 * @return or <code>null</code> if none matches 
	 */
	public RenderingProcessor resolverRenderingProcessor(String url);
	
	/**
	 * Discovers and returns the HttpProcessor that can, according to it's registred UrlMapping, process the given url 
	 * @param url
	 * @return or <code>null</code> if none matches 
	 */
	public HttpProcessor resolveControlProcessor(String url);
	
	/**
	 * Set the availability state of this service.
	 * 
	 * If the service is available requests will be processed.
	 * If not, a TEMPORARY_UNAVAILABLE HTTP error code will be returned.
	 * @param available
	 */
	public void setAvailable(boolean available);
	
	/**
	 * 
	 * @return {@code true} if the service is available, {@code false} otherwise
	 */
	public boolean isAvailable();
	
	
	/**
	 * Initialize the server and put in an available state
	 * 
	 *  @see #setAvailable(boolean)
	 */
	public void start();
	
	/**
	 * Renders the server inert. No further requests will be processed.
	 * After calling this method this service instance is useless and should be removed.
	 */
	public void stop();
	
}
