/**
 * 
 */
package org.middleheaven.process.web.client.apache;


import org.apache.http.impl.client.DefaultHttpClient;
import org.middleheaven.process.web.client.HttpClient;
import org.middleheaven.process.web.client.HttpClientConfiguration;
import org.middleheaven.process.web.client.HttpClientService;

/**
 * 
 */
public class ApacheHttpClientService implements HttpClientService {

	
	public ApacheHttpClientService (){
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpClient getClient(HttpClientConfiguration configuration) {
		DefaultHttpClient apacheClient = new DefaultHttpClient();
		
		//apacheClient.setCredentialsProvider(credsProvider)
		
		return new HttpClientAdapter(apacheClient);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 */
	public void start() {
		// TODO Auto-generated method stub
		
	}
}
