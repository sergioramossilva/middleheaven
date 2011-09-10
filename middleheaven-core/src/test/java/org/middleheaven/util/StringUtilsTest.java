package org.middleheaven.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringUtilsTest {

	
	@Test
	public void testSimplePattern(){
		
		String phrase = "MiddleHeaven is the best framework";
		
		assertTrue(StringUtils.simplePatternMatch("*framework", phrase));
		assertTrue(StringUtils.simplePatternMatch("MiddleHeaven*", phrase));
		assertTrue(StringUtils.simplePatternMatch("*best*", phrase));
	}
	

}
