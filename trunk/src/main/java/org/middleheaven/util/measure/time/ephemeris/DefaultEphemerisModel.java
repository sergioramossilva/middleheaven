package org.middleheaven.util.measure.time.ephemeris;

import java.util.Collections;
import java.util.Set;

import org.middleheaven.util.measure.time.DateHolder;


public final class DefaultEphemerisModel extends EphemerisModel {

	@Override
	public boolean isHoliday(DateHolder dateHolder) {
		return false;
	}

	@Override
	public Set<Ephemeris> getEphemeris(DateHolder date) {
		return Collections.emptySet();
	}



}
