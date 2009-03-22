package org.middleheaven.crypto;

import java.util.LinkedList;
import java.util.List;

/**
 * Allows for several algorithm to be chain together to produce another
 * cipher algorithm.
 */
public class CompositeCipherAlgorithm implements CipherAlgorithm {

	final List<CipherAlgorithm> ciphers = new LinkedList<CipherAlgorithm>();
	
	public final CompositeCipherAlgorithm addCipher(CipherAlgorithm other){
		ciphers.add(other);
		return this;
	}
	
	
	@Override
	public final byte[] cipher(byte[] message) {
        byte[] result = message; 
		for (CipherAlgorithm c: ciphers){
			result = c.cipher(result);
		}
		return result;
	}

}
