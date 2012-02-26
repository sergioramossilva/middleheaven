/**
 * 
 */
package org.middleheaven.process.web.client;

import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;
import org.middleheaven.process.web.client.apache.ApacheHttpClientService;

/**
 * 
 */
public class HttpClientServiceActivator extends Activator{
	
	private HttpClientService service;

	@Publish
	public HttpClientService getHttpClientService(){
		return service;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void activate() {
		ApacheHttpClientService apacheService = new ApacheHttpClientService();
		
		apacheService.start();
		
		this.service = apacheService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inactivate() {
		service.close();
	}

}
