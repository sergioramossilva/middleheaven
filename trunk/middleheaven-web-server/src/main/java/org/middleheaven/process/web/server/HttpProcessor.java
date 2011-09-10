package org.middleheaven.process.web.server;

import org.middleheaven.process.web.HttpProcessException;
import org.middleheaven.process.web.UrlMapping;

/**
 * Process an HTTP (HiperText Transport Protocol) request.
 * 
 * One or more {@link HttpProcessor} are registered with the {@link HttpServerService} to process
 * requests directed at specific URL address in the server. 
 * 
 * @see  HttpServerService#registerHttpProcessor(String, HttpProcessor, UrlMapping)
 */
public interface HttpProcessor  {

	/**
	 * Process the request 
	 * @param context the invocation context.
	 * 
	 * @throws HttpProcessException if something goes wrong.
	 * @return the process outcome.
	 */
	public Outcome process(HttpServerContext context) throws HttpProcessException;
}
