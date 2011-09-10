/**
 * 
 */
package org.middleheaven.process.web;

import java.util.List;

import org.middleheaven.aas.Subject;
import org.middleheaven.global.Culture;
import org.middleheaven.process.AttributeContext;

/**
 * Aggregates all information container in a HTTP request.
 */
public interface HttpRequest {

	/**
	 * allows access to the current state attributes context.
	 * 
	 * @return the {@link AttributeContext}.
	 */
	public AttributeContext getAttributes();
	
	/**
	 * @return
	 */
	public HttpEntry getEntry();

	/**
	 * The list of {@link Culture}s accepted by the user agent. 
	 * The list is ordered according to rules of the Accept-Language HTTP header.
	 * @return a list of accepted {@link Culture}s.
	 */
	public List<Culture> getCultures();
	
	/**
	 * The most likely {@link Culture} accepted by the user agent. 
	 * Selects the most preferable {@link Culture} according to the rules of the Accept-Language HTTP header.
	 * 
	 * @return The most likely {@link Culture} accepted by the user agent.
	 */
	public Culture getCulture();
	
	/**
	 * the current {@link HttpChannel}.
	 * @return the current communication channel.
	 */
	public HttpChannel getHttpChannel();
	

	/**
	 * 
	 * @return an {@link HttpUrl} object with the current request url. 
	 */
	public HttpUrl getRequestUrl();
	
	/**
	 * 
	 * @return the origin of the request.
	 */
	public HttpUrl getRefererUrl();

	
	/**
	 * The {@link HttpMethod} that being called from the user agent.
	 * @return the {@link HttpMethod} that being called from the user agent.
	 */
	public  HttpMethod getMethod();

	/**
	 * 
	 * @return The current authenticated {@link Subject} or <code>null</code> if none exists.
	 */
	public Subject getAuthenticatedSubject();
	
	// TODO add Servlet 3.0 methods


}
	
