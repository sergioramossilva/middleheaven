/**
 * 
 */
package org.middleheaven.process.web.server;

import org.middleheaven.process.web.UrlPattern;

/**
 * 
 */
public interface HttpProcessorConfig {

	/**
	 * 
	 * @return
	 */
	public String getProcessorId();
	/**
	 * 
	 * @return
	 */
	public HttpServerService getRegisteredService();
	/**
	 * 
	 * @return
	 */
	public UrlPattern getUrlPattern();
	
}
