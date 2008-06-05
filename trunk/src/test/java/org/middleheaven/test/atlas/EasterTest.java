package org.middleheaven.test.atlas;

import static org.junit.Assert.*;

import org.junit.Test;
import org.middleheaven.global.calendar.EasterBasedCalculatedCalendarModel;
import org.middleheaven.util.measure.time.CalendarDate;
import org.middleheaven.util.measure.time.DateHolder;
import org.middleheaven.util.measure.time.EphemeridModel;


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
		
		assertEquals(CalendarDate.date(2008,6,6),model.getOrdinalWorkingDayOfMonth(new MonthOfYear(2008,6), 5));
	}
	
	@Test
	public void testEeasterCalculation(){
		
		EasterBasedCalculatedCalendarModel model = new EasterBasedCalculatedCalendarModel();
		
		XCalendarDate [] easter = {
				new XCalendarDate(1992,4,19),
				new XCalendarDate(1993,4,11),
				new XCalendarDate(1994,4,3),
				new XCalendarDate(1995,4,16),
				new XCalendarDate(1996,4,7),
				new XCalendarDate(1997,3,30),
				new XCalendarDate(1998,4,12),
				new XCalendarDate(1999,4,4),
				new XCalendarDate(2000,4,23),
				new XCalendarDate(2001,4,15),
				new XCalendarDate(2002,3,31),
				new XCalendarDate(2003,4,20),
				new XCalendarDate(2004,4,11),
				new XCalendarDate(2005,3,27),
				new XCalendarDate(2006,4,16),
				new XCalendarDate(2007,4,8),
				new XCalendarDate(2008,3,23),
				new XCalendarDate(2009,4,12),
				new XCalendarDate(2010,4,4),
				new XCalendarDate(2011,4,24),
				new XCalendarDate(2012,4,8),
				new XCalendarDate(2013,3,31)
		};
		
		for (int i=0; i < easter.length;i++){
			assertEquals(	easter[i] , model.calculaEaster(easter[i].year()));
		}
		
		// Good Friday
		assertTrue (model.hasEphemeris(new XCalendarDate(2002,3,29)));
		// Ash Wednesday
		assertTrue (model.hasEphemeris(new XCalendarDate(2000,3,8)));
		//  Ascension  Day
		assertTrue (model.hasEphemeris(new XCalendarDate(2001,5,24)));

	}
}
