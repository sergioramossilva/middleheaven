package org.middleheaven.util.convertion;

import static org.junit.Assert.*;

import org.junit.Test;
import org.middleheaven.util.conversion.ConvertionException;
import org.middleheaven.util.conversion.TypeConvertions;

public class ConvertionTest {

	@Test
	public void testPrimitiveConvertion (){
		
		Integer intA = 1;
		Object intCa = TypeConvertions.convert(intA, int.class);
		
		assertTrue(intCa instanceof Integer);
		assertEquals(intA,intCa);
		
		Long longA = 1L;
		Object longCa = TypeConvertions.convert(longA, int.class);
		
		assertTrue(longCa instanceof Integer);
		assertTrue(longA.intValue() == ((Number)longCa).intValue());
		
		
	} 
	
	@Test(expected=ConvertionException.class)
	public void testOverflowPrimitiveConvertion (){
	
		Long longA = Integer.MAX_VALUE + 3L;
		// this long cannot be holded by an int
		Object longCa = TypeConvertions.convert(longA, int.class);
		
		assertTrue(longCa instanceof Integer);
		assertTrue(longA.intValue() == ((Number)longCa).intValue());
		
	}
}
