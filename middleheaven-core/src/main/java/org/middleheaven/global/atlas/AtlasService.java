package org.middleheaven.global.atlas;

import java.util.Collection;

import org.middleheaven.core.wiring.service.Service;


public interface AtlasService {

	public Country findCountry(String isoCode);
	public Collection<Country> findALLCountries();
	public Town findTown(Country country , String name);
	public Town findTown(String isoCountryCode , String name);
}
