package org.middleheaven.web.processing;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.middleheaven.global.CultureResolver;

/**
 * Process a Http request.
 * 
 * One or more {@code HttpProcessor} is registered with the {@code HttpServerService} to process
 * requests directed at specific URL address in the server. 
 */
public interface HttpProcessor extends CultureResolver {

	
	public void setCultureResolveStrategy(HttpCultureResolveStrategy strategy);


	/**
	 * Process the request 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void process(HttpServletRequest request ,HttpServletResponse response) throws HttpProcessException;
}
