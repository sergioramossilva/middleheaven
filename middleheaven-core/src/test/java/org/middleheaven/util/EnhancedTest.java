package org.middleheaven.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.middleheaven.quantity.math.Real;
import org.middleheaven.util.collections.NumberAcumulator;
import org.middleheaven.util.collections.Range;

public class EnhancedTest {

	
	@Test
	public void testDice(){
		
		Integer i = Range.over(1, 6).random();
		
		assertNotNull(i);
	}
	
	@Test
	public void testAcumulator(){
		
		NumberAcumulator<Real> acumulator = NumberAcumulator.instance();
		Range.over(Real.ONE(), Real.valueOf(6),Real.ONE()).each( acumulator);

	
		assertEquals(Real.valueOf(21), acumulator.getResult());
		
		acumulator.reset();
		Real.ONE().upTo(6).each( acumulator);
		
		assertEquals(Real.valueOf(21), acumulator.getResult());
	}
	
}
