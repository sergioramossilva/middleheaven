package org.middleheaven.global.calendar;


import org.middleheaven.global.atlas.AtlasLocale;
import org.middleheaven.utils.time.DateHolder;

public interface EphemerisService {

	
	public Ephemeris getEphemeris (DateHolder holder , AtlasLocale locale);
}
