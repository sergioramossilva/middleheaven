package org.middleheaven.test.clock;

import static org.junit.Assert.*;

import org.junit.Test;
import org.middleheaven.util.measure.time.CalendarDate;
import org.middleheaven.util.measure.time.DateHolder;
import org.middleheaven.util.measure.time.DayOfWeek;
import org.middleheaven.util.measure.time.Month;
import org.middleheaven.util.measure.time.ephemeris.EasterBasedCalculatedCalendarModel;
import org.middleheaven.util.measure.time.ephemeris.EphemeridModel;


public class EasterTest {

	@Test
	public void testWorkingDays(){
		EphemeridModel model = new EasterBasedCalculatedCalendarModel();
		
		DateHolder start = CalendarDate.date(2008,5,28);
		DateHolder end = CalendarDate.date(2008,6,4);
		
		assertEquals(end, model.addWorkingDays(5, start));
		assertEquals(start, model.subtractWorkingDays(5, end));
		
		start = CalendarDate.date(2008,6,2);
		end = CalendarDate.date(2008,6,9);
		
		assertEquals(end, model.addWorkingDays(5, start));
		assertEquals(start, model.subtractWorkingDays(5, end));
		
		assertEquals(CalendarDate.date(2008,6,6),model.getOrdinalWorkingDayOfMonth(Month.ofYear(2008,6), 5));
	}
	
	@Test
	public void testEeasterCalculation(){
		
		EasterBasedCalculatedCalendarModel model = new EasterBasedCalculatedCalendarModel();
		
		// from http://users.sa.chariot.net.au/~gmarts/eastcalc.htm
		CalendarDate [] easter = {
				CalendarDate.date(1992,4,19),
				CalendarDate.date(1993,4,11),
				CalendarDate.date(1994,4,3),
				CalendarDate.date(1995,4,16),
				CalendarDate.date(1996,4,7),
				CalendarDate.date(1997,3,30),
				CalendarDate.date(1998,4,12),
				CalendarDate.date(1999,4,4),
				CalendarDate.date(2000,4,23),
				CalendarDate.date(2001,4,15),
				CalendarDate.date(2002,3,31),
				CalendarDate.date(2003,4,20),
				CalendarDate.date(2004,4,11),
				CalendarDate.date(2005,3,27),
				CalendarDate.date(2006,4,16),
				CalendarDate.date(2007,4,8),
				CalendarDate.date(2008,3,23),
				CalendarDate.date(2009,4,12),
				CalendarDate.date(2010,4,4),
				CalendarDate.date(2011,4,24),
				CalendarDate.date(2012,4,8),
				CalendarDate.date(2013,3,31)
		};
		
		for (int i=0; i < easter.length;i++){
			assertEquals(	easter[i] , EasterBasedCalculatedCalendarModel.calculaEaster(easter[i].year()));
		}
		
		// Good Friday
		assertTrue (model.hasEphemeris(CalendarDate.date(2002,3,29)));
		// Ash Wednesday
		assertTrue (CalendarDate.date(2000,3,8).dayOfWeek().isWednesday());
		assertTrue (model.hasEphemeris(CalendarDate.date(2000,3,8)));
		
		//  Ascension  Day
		assertTrue (model.hasEphemeris(CalendarDate.date(2001,5,24)));

	}
}
