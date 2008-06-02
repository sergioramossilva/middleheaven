package org.middleheaven.global.calendar;

import java.util.Collections;
import java.util.Set;

import org.middleheaven.utils.time.DateHolder;

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
		return date.getDayOfWeek();
	}

}
