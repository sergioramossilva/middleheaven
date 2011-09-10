package org.middleheaven.global.atlas;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.global.Language;

public abstract class AtlasModule {

	Map<String , List<Language> > countryLocales;
	
	protected synchronized Language findLocaleForCountry(String isoCode){
		if (countryLocales==null){
			// load locales
			final Locale[] locales = Locale.getAvailableLocales();
			countryLocales = new TreeMap<String , List<Language> >();
			for (Locale locale : locales ){
				List<Language> list = countryLocales.get(locale.getCountry());
				if (list==null){
					list=new ArrayList<Language>();
					countryLocales.put(locale.getCountry(), list);
				}
				list.add(Language.valueOf(locale.getLanguage()));
			}
		}
		
		List<Language> list =  countryLocales.get(isoCode);
		if (list==null || list.isEmpty()){
			return Language.valueOf("");
		} else {
			return list.get(0);
		}
		
		
	
	}
	
	
	public abstract void loadAtlas(AtlasContext context);
}
