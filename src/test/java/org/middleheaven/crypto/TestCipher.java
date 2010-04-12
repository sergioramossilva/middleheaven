package org.middleheaven.crypto;

import static org.junit.Assert.*;

import org.junit.Test;


public class TestCipher {

	
	@Test
	public void testHexCipher(){
		
		HexCipherAlgorithm cipher = new HexCipherAlgorithm();
		
		String s = new String(cipher.cipher(new byte[]{2,15}));
		
		assertEquals("20F", s);
	}
}
