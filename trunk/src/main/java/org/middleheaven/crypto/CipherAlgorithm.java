package org.middleheaven.crypto;


/**
 * Main contract for a cipher algorithm
 */
public interface CipherAlgorithm {

	/**
	 * ciphers the {@code message} according to the algorithm
	 * 
	 * @param message
	 * @return
	 * @throws CipherException
	 */
	public byte[] cipher (byte[] message) throws CipherException;
}
