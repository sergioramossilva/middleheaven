package org.middleheaven.quantity.time;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestCompareDates {

	
	@Test
	public void testCompare(){
		
		CalendarDate a = CalendarDate.date(2012, 4, 10);
		CalendarDate b = CalendarDate.date(2010, 4, 10);
		
		assertTrue(b.isBefore(a));
		assertFalse(a.isBefore(b));
		
		CalendarDate c = CalendarDate.date(2012, 5, 10);
		CalendarDate d = CalendarDate.date(2012, 4, 10);
		
		assertTrue(d.isBefore(c));
		assertFalse(c.isBefore(d));
		
	}
}
