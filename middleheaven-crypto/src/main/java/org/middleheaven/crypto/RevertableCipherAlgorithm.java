package org.middleheaven.crypto;

/**
 * Represents and {@link CipherAlgorithm} that can be reverted, i.e. deciphered. 
 */
public interface RevertableCipherAlgorithm extends CipherAlgorithm {

	/**
	 * Decipher the message.
	 * @param message message to decipher.
	 * @return the deciphered message
	 * @throws CipherException
	 */
	public byte[] revertCipher (byte[] message) throws CipherException;
	
}
