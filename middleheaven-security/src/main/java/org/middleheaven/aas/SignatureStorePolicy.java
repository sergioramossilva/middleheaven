/**
 * 
 */
package org.middleheaven.aas;

/**
 * 
 */
public interface SignatureStorePolicy<C> {

	
	public SignatureStore resolveSignatureStore(C context);
}
