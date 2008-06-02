package org.middleheaven.global.atlas;

import java.util.Collection;


public interface AtlasService {

	public Country findCountry(String isoCode);
	public Collection<Country> findALLCountries();
	public Town findCity(Country country , String name);
	public Town findCity(String isoCountryCode , String name);
}
