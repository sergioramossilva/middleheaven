package org.middleheaven.global.atlas;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public abstract class AtlasModule {

	Map<String , List<Locale> > countryLocales;
	
	protected synchronized Locale findLocaleForCountry(String isoCode){
		if (countryLocales==null){
			final Locale[] locales = Locale.getAvailableLocales();
			countryLocales = new TreeMap<String , List<Locale> >();
			for (Locale locale : locales ){
				List<Locale> list = countryLocales.get(locale.getCountry());
				if (list==null){
					list=new ArrayList<Locale>();
					countryLocales.put(locale.getCountry(), list);
				}
				list.add(locale);
			}
		}
		
		List<Locale> list =  countryLocales.get(isoCode);
		if (list==null || list.isEmpty()){
			return new Locale("",isoCode);
		} else {
			return list.get(0);
		}
		
		
	
	}
	
	
	public abstract void loadAtlas(AtlasContext context);
}
