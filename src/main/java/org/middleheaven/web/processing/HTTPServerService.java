package org.middleheaven.web.processing;

/**
 * 
 *
 */
public interface HTTPServerService {

	
	public void register(String processorID, HttpProcessor procesor, UrlMapping mapping);
	public void unRegister(String processorID);
	
	/**
	 * Discovers and returns the HttpProcessor that can, according to it's registred UrlMapping, process the given url 
	 * @param url
	 * @return or <code>null</code> if none matches 
	 */
	public HttpProcessor processorFor(String url);
}
