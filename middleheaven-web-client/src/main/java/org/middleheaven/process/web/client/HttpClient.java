/**
 * 
 */
package org.middleheaven.process.web.client;

import org.middleheaven.process.web.HttpProcessException;
import org.middleheaven.process.web.UserAgent;

/**
 * 
 */
public interface HttpClient {

	
	public void setUserAgent(UserAgent userAgent);

	/**
	 * Send a request for processing.
	 * @param request the request to process.
	 * @return the processed response.
	 */
	public HttpClientResponse process(HttpClientRequest request) throws HttpProcessException ;
}
