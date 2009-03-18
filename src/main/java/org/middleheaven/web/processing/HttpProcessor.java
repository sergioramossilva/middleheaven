package org.middleheaven.web.processing;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Process a Http request.
 * 
 * HttpProcessor is registered with the {@code HttpServerService} to process
 * requests directed at specific URL address in the server. 
 */
public interface HttpProcessor {

	/**
	 * Process the request 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void process(HttpServletRequest request ,HttpServletResponse response) throws IOException;
}
