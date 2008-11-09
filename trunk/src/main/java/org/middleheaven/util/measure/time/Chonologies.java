package org.middleheaven.util.measure.time;

import org.middleheaven.global.Culture;
import org.middleheaven.util.measure.time.chono.JavaCalendarCronology;

public class Chonologies {

	
	
	public static Chronology getChonology(Culture culture){
		return new JavaCalendarCronology(culture.toLocale());
	}
}
