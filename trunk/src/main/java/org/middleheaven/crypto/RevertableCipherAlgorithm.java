package org.middleheaven.crypto;

public interface RevertableCipherAlgorithm extends CipherAlgorithm {

	public byte[] revertCipher (byte[] message) throws CipherException;
	
}
