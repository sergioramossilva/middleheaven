package org.middleheaven.util.measure.time;

import java.util.Collections;
import java.util.Set;

import org.middleheaven.global.calendar.Ephemeris;

public class DefaultEphemeridModel extends EphemeridModel {

	@Override
	public boolean isHoliday(DateHolder dateHolder) {
		return false;
	}

	@Override
	public Set<Ephemeris> getEphemeris(DateHolder date) {
		return Collections.emptySet();
	}



}
