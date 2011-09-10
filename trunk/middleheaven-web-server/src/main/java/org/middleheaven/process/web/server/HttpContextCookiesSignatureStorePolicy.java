/**
 * 
 */
package org.middleheaven.process.web.server;

import org.middleheaven.aas.SignatureStore;
import org.middleheaven.quantity.time.Period;

/**
 * 
 */
public class HttpContextCookiesSignatureStorePolicy implements HttpServerSignatureStorePolicy {

	
	private Period maxLifePeriod = Period.minutes(30);
	
	public HttpContextCookiesSignatureStorePolicy (){
		
	}
	
	
	public void setMaxLifePeriod(Period maxLifePeriod){
		this.maxLifePeriod = maxLifePeriod;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SignatureStore resolveSignatureStore(HttpServerContext context) {
		return new HttpContextCookiesSignatureStore(context, maxLifePeriod);
	}

}
