/**
 * 
 */
package org.middleheaven.process.web.server;

import org.middleheaven.process.web.HttpRequest;
import org.middleheaven.process.web.HttpUserAgent;

/**
 * 
 */
public interface HttpServerRequest extends HttpRequest {

	/**
	 * Information on the user agent that made the request.
	 * @return a {@link HttpUserAgent} object with the current user agent. 
	 */
	public  HttpUserAgent getAgent();
	

}
