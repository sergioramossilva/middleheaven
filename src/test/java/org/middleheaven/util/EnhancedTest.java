package org.middleheaven.util;

import static org.junit.Assert.*;

import org.junit.Test;
import org.middleheaven.util.collections.Range;

public class EnhancedTest {

	
	@Test
	public void testDice(){
		
		Integer i = Range.over(1, 6).random();
		
		assertNotNull(i);
	}
	
}
