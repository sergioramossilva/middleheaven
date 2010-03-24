package org.middleheaven.web.processing;

import java.io.IOException;

/**
 * Process a Http request.
 * 
 * One or more {@code HttpProcessor} is registered with the {@code HttpServerService} to process
 * requests directed at specific URL address in the server. 
 */
public interface HttpProcessor  {

	/**
	 * Process the request 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public Outcome process(HttpContext context) throws HttpProcessException;
}
