/**
 * 
 */
package org.middleheaven.process.web.server;

import org.middleheaven.process.web.UrlPattern;

/**
 * 
 */
public interface HttpProcessorConfig {

	
	public String getProcessorId();
	public HttpServerService getRegisteredService();
	public UrlPattern getUrlPattern();
	
}
