package org.middleheaven.quantity.time.ephemeris;


import org.middleheaven.global.atlas.AtlasLocale;
import org.middleheaven.quantity.time.DateHolder;

public interface EphemerisService {

	
	public Ephemeris getEphemeris (DateHolder holder , AtlasLocale locale);
}
