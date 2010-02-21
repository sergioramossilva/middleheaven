package org.middleheaven.test.time;

import static org.junit.Assert.*;

import org.junit.Test;
import org.middleheaven.quantity.time.Period;

public class TimeTest {

	
	@Test
	public void testPeriodUnits(){
		
		Period period = Period.nanoseconds(20);
		
		assertEquals("20 ± 1 ns", period.toString());
	}
}
