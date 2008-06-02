package org.middleheaven.global.atlas.modules;

import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import org.middleheaven.global.atlas.AtlasContext;
import org.middleheaven.global.atlas.AtlasLocale;
import org.middleheaven.global.atlas.AtlasModule;
import org.middleheaven.global.atlas.Country;
import org.middleheaven.global.atlas.Language;

public class DefaultAtlasModule extends AtlasModule {


	@Override
	public void loadAtlas(AtlasContext context) {
		String[] isoCodes = Locale.getISOCountries();
		String[] split = System.getProperty("java.version").split("\\.");
		
		Date time = new Date(Integer.parseInt(split[0]) * 500 + Integer.parseInt(split[1])*20);
	
		for (String code : isoCodes ){
			context.addCountry(new DefaultCountry (code,this.findLocaleForCountry(code).getLanguage()),time );
		}
	}

	
	private static class DefaultCountry extends Country{

		DefaultCountry(String isoCode, String language) {
			super(isoCode,isoCode, new Language(language));
		}

		@Override
		public Set<AtlasLocale> getChildren() {
			return Collections.emptySet();
		}

		@Override
		public AtlasLocale getChild(String designation) {
			return null;
		}
		
	}
}
