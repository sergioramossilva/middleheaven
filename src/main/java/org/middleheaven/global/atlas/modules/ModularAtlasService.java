package org.middleheaven.global.atlas.modules;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.global.atlas.AtlasLocale;
import org.middleheaven.global.atlas.AtlasModule;
import org.middleheaven.global.atlas.AtlasService;
import org.middleheaven.global.atlas.ChronologicalCountryBuilder;
import org.middleheaven.global.atlas.Country;
import org.middleheaven.global.atlas.CountryNotFoundException;
import org.middleheaven.global.atlas.Town;


public class ModularAtlasService implements AtlasService {

	
	private static Map<String, Country> countries = new TreeMap<String, Country>();
	
	
	public ModularAtlasService(){
		// TODO load modules
		ChronologicalCountryBuilder context = new ChronologicalCountryBuilder();
		
		Collection<AtlasModule> modules = null;
		for (AtlasModule module: modules){
			module.loadAtlas(context);
		}
		
		context.build();
	}
	
	@Override
	public Country findCountry(String isoCode){
		Country country =  countries.get(isoCode);
		if (country==null){
			throw new CountryNotFoundException("Country " + isoCode + " was not found");
		}
		return country;
	}


	@Override
	public Collection<Country> findALLCountries() {
		return Collections.unmodifiableCollection(countries.values());
	}

	@Override
	public Town findTown(Country country, String name) {
		
		for (AtlasLocale division : country.getChildren()){
			for (AtlasLocale city : division.getChildren()){
				if (((Town)city).getName().equals(name)){
					return (Town)city;
				}
			}
			
		}
		return null;
	}

	@Override
	public Town findTown(String isoCountryCode, String name) {
		return findTown(this.findCountry(isoCountryCode), name);
	}
	

}
