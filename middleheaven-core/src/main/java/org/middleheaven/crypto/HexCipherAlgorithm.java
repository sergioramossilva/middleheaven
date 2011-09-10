package org.middleheaven.crypto;

import java.math.BigInteger;

public class HexCipherAlgorithm implements CipherAlgorithm {

	@Override
	public byte[] cipher(byte[] message) throws CipherException {
		
		BigInteger n = new BigInteger(message);

		return n.toString(16).getBytes();
	}



}
