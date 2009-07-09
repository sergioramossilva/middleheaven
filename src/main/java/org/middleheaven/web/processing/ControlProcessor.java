package org.middleheaven.web.processing;

import java.io.IOException;

import org.middleheaven.global.CultureResolver;

/**
 * Process a Http request.
 * 
 * One or more {@code HttpProcessor} is registered with the {@code HttpServerService} to process
 * requests directed at specific URL address in the server. 
 */
public interface ControlProcessor extends CultureResolver {

	
	public void setCultureResolveStrategy(HttpCultureResolveStrategy strategy);


	/**
	 * Process the request 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public Outcome process(HttpContext context) throws HttpProcessException;
}
