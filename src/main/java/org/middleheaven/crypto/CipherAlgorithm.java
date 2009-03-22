package org.middleheaven.crypto;

public interface CipherAlgorithm {

	
	public byte[] cipher (byte[] message) throws CipherException;
}
