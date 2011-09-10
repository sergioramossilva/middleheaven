package org.middleheaven.process.web.client;
/**
 * 
 */

/**
 * 
 */
public interface HttpClientService {

	
	public HttpClient getClient(HttpClientConfiguration configuration);

	/**
	 * 
	 */
	public void close();
}
