package org.middleheaven.global.calendar;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.middleheaven.utils.time.XCalendarDate;
import org.middleheaven.utils.time.DateHolder;

/**
 * Determines Easter ephemeris. 
 *
 * Days marked as Saturday, Sunday and Easter are considered non-working days 
 */
public class EasterBasedCalculatedCalendarModel extends CalendarModel {

	
	protected boolean isWorkingDay(Set<Ephemeris> ephemeris){
		for (Ephemeris e : ephemeris){
			if (!e.isWorkingDay()){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param year
	 * @return
	 * @see http://www.tondering.dk/claus/cal/node3.html#SECTION003132000000000000000
	 */
	public static DateHolder calculaEaster(int year){
		int G = year % 19;
		int C = year / 100;
		int H = (C-C/4 - (8*C+13)/25 + 19* G + 15) % 30;
		int I = H - (H/28) * (1 - (29 / (H+1))* ((21-G)/11));
		int J = (year + year/4 + I + 2 - C + C/4) % 7;
		int L = I - J;
		
		int month = 3 + (L + 40)/44;
		int day = L + 28 - 31 *(month/4);
		
		return new XCalendarDate(year,month,day);
	}
	
	Map<DateHolder, Ephemeris> dateEphemeris = new HashMap<DateHolder, Ephemeris>();
	
	@Override
	public Set<Ephemeris> getEphemeris(DateHolder date) {
		Ephemeris dayEphemeris = dateEphemeris.get(date);
		
		if (dayEphemeris==null){
			DateHolder easterSunday =calculaEaster(date.year());
			dateEphemeris.put(easterSunday, new DefaultEphemeris("Easter",false));
		
			// TODO use real Dateholder
			
			// 2 days before easter
			DateHolder goodFriday = easterSunday.subtract(2);
			dateEphemeris.put(goodFriday, new DefaultEphemeris("Good Friday",true));
			
			// 42 days before easter
			DateHolder ashWendnesday = easterSunday.subtract(42).reduceTo(Calendar.WEDNESDAY);

			dateEphemeris.put(ashWendnesday, new DefaultEphemeris("Ash Wednesday",true));
			
			// 7 weeks (49 days) after easter
			DateHolder whitSunday = easterSunday.add(49);
			dateEphemeris.put(whitSunday, new DefaultEphemeris("Pentecost",false));
			
			// 39 days after easter
			DateHolder ascensionDay = whitSunday.subtract(10);
			dateEphemeris.put(ascensionDay, new DefaultEphemeris("Ascension Day",false));
			
			// 43 days before easter , 1 day before Ash Wednesday
			DateHolder carnival = ashWendnesday.subtract(1);
			dateEphemeris.put(carnival, new DefaultEphemeris("Carnival",false));
			
		} 

	    dayEphemeris = dateEphemeris.get(date);
		if (dayEphemeris==null){
			return Collections.emptySet();
		} else {
			return Collections.singleton(dayEphemeris);
		}

	}

	@Override
	public boolean hasEphemeris(DateHolder date) {
		return !getEphemeris(date).isEmpty();
	}

	@Override
	public boolean isWorkingDay(DateHolder date) {
		 return !date.isWeekDay(Calendar.SATURDAY) && !date.isWeekDay(Calendar.SUNDAY) && isWorkingDay(getEphemeris(date));
	}

}
