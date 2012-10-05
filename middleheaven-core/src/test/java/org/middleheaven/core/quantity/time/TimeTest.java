/**
 * 
 */
package org.middleheaven.core.quantity.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.middleheaven.quantity.time.CalendarDate;
import org.middleheaven.quantity.time.Period;

/**
 * 
 */
public class TimeTest {

	@Test
	public void test() {
		
		CalendarDate a = CalendarDate.date(2010, 1, 1);

		assertEquals(1, a.upTo(a).size());
		
		
		CalendarDate b = CalendarDate.date(2012, 6, 20);
		
		assertTrue(a.upTo(b).size() > 30);
		
		
		CalendarDate c = CalendarDate.date(2012, 6, 10);
		
		assertEquals(11, c.upTo(b).size());
	}

	
	@Test
	public void testPeriodUnits(){
		
		Period period = Period.nanoseconds(20);
		
		assertEquals("(20 ± 1) ns", period.toString());
	}
}
