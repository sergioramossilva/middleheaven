package org.middleheaven.global.calendar;

import java.util.Collections;
import java.util.Set;

import org.middleheaven.util.measure.time.DateHolder;
import org.middleheaven.util.measure.time.DayOfWeek;

/**
 * Ephemeris unaware calendar model. No day is considered to have any ephemeris.
 * Days marked as Saturday and Sunday are considered non-working days 
 *
 */
public class DefaultCalendarModel extends CalendarModel {

	@Override
	public Set<Ephemeris> getEphemeris(DateHolder date) {
		return Collections.emptySet();
	}

	@Override
	public boolean hasEphemeris(DateHolder date) {
		return false;
	}

	@Override
	public boolean isWorkingDay(DateHolder date) {
		return !(date.dayOfWeek().equals(DayOfWeek.SATURDAY) || date.dayOfWeek().equals(DayOfWeek.SUNDAY));
	}

}
