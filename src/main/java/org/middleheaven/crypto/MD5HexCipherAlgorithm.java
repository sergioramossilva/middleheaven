package org.middleheaven.crypto;

public class MD5HexCipherAlgorithm extends CompositeCipherAlgorithm {

	
	public MD5HexCipherAlgorithm(){
		this.addCipher(new MD5CipherAlgorithm())
			.addCipher(new HexCipherAlgorithm());
	}
}
