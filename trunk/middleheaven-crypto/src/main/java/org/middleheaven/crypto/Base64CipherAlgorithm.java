package org.middleheaven.crypto;

/**
 * Provides Base64 encryption. 
 * 
 * Although Base64 is not a real encryption algorithm (because as no secret)
 * it can be comprehended as a no-key reversible cipher algorithm. 
 *   
 */
public final class Base64CipherAlgorithm implements RevertableCipherAlgorithm {
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public byte[] cipher(byte[] message) {
		if (message==null){
			throw new IllegalArgumentException();
		}
		return Base64.encode(message);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public byte[] revertCipher(byte[] message) throws CipherException {
		if (message==null){
			throw new IllegalArgumentException();
		}
		return Base64.decode(message);
	}

}
