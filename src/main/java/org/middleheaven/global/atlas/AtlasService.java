package org.middleheaven.global.atlas;

import java.util.Collection;


public interface AtlasService {

	public Country findCountry(String isoCode);
	public Collection<Country> findALLCountries();
	public City findCity(Country country , String name);
	public City findCity(String isoCountryCode , String name);
}
