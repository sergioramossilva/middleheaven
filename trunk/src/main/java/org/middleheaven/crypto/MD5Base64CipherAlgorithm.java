package org.middleheaven.crypto;

public class MD5Base64CipherAlgorithm extends CompositeCipherAlgorithm {

	
	public MD5Base64CipherAlgorithm(){
		this.addCipher(new MD5CipherAlgorithm())
			.addCipher(new Base64CipherAlgorithm());
	}
}
