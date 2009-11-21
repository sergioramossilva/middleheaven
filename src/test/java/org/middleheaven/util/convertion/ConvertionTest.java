package org.middleheaven.util.convertion;

import static org.junit.Assert.*;

import org.junit.Test;
import org.middleheaven.util.coersion.CoersionException;
import org.middleheaven.util.coersion.TypeCoercing;

public class ConvertionTest {

	@Test
	public void testPrimitiveConvertion (){
		
		Integer intA = 1;
		Object intCa = TypeCoercing.convert(intA, int.class);
		
		assertTrue(intCa instanceof Integer);
		assertEquals(intA,intCa);
		
		Long longA = 1L;
		Object longCa = TypeCoercing.convert(longA, int.class);
		
		assertTrue(longCa instanceof Integer);
		assertTrue(longA.intValue() == ((Number)longCa).intValue());
		
		
	} 
	
	@Test(expected=CoersionException.class)
	public void testOverflowPrimitiveConvertion (){
	
		Long longA = Integer.MAX_VALUE + 3L;
		// this long cannot be holded by an int
		Object longCa = TypeCoercing.convert(longA, int.class);
		
		assertTrue(longCa instanceof Integer);
		assertTrue(longA.intValue() == ((Number)longCa).intValue());
		
	}
}
