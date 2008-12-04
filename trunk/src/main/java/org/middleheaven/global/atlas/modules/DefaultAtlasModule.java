package org.middleheaven.global.atlas.modules;

import java.util.Locale;

import org.middleheaven.global.atlas.AtlasContext;
import org.middleheaven.global.atlas.AtlasModule;
import org.middleheaven.global.atlas.CountryInfo;
import org.middleheaven.util.measure.time.CalendarDate;
import org.middleheaven.util.measure.time.DateHolder;

public class DefaultAtlasModule extends AtlasModule {


	@Override
	public void loadAtlas(AtlasContext context) {
		String[] isoCodes = Locale.getISOCountries();
		String[] split = System.getProperty("java.version").split("\\.");
		
		DateHolder time = CalendarDate.date(1900 + Integer.parseInt(split[0]) ,Integer.parseInt(split[1]) ,1);
		
		for (String code : isoCodes ){
			CountryInfo info = new CountryInfo(code , time);
			info.setName(code);
			
			info.addLanguage(this.findLocaleForCountry(code).getLanguage());
			context.addCountryInfo(info);
		}
		
		
	}

	
}
