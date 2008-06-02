package org.middleheaven.global.atlas.modules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.middleheaven.global.atlas.AtlasContext;
import org.middleheaven.global.atlas.AtlasLocale;
import org.middleheaven.global.atlas.AtlasModule;
import org.middleheaven.global.atlas.Language;
import org.middleheaven.global.atlas.Town;
import org.middleheaven.global.atlas.Country;
import org.middleheaven.global.atlas.CountryDivision;

public class ISOFileAtlasModule extends AtlasModule {

	String path = "data";

	public ISOFileAtlasModule() {

		String atlaspath = System.getProperty("middleheaven.atlas.data.path");
		if (atlaspath!=null){
			
			try {
				File file = new File(atlaspath.replaceAll("file:/", "")).getCanonicalFile();
				path = file.getAbsolutePath();
			} catch (IOException e) {

			}
			
		} 

	}

	public InputStream locate(String filename) {
		try {
			File f = new File( path  + File.separator + filename);
			return new FileInputStream (f);
		} catch (FileNotFoundException e) {
			return this.getClass().getResourceAsStream("/org/middleheaven/global/atlas/data/" + filename);
		}
	}


	@Override
	public void loadAtlas(AtlasContext context) {
		InputStream in = locate("iso3166.txt");
		if (in!=null){
			try {

				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				int year = Integer.parseInt(reader.readLine());
				Date date = new Date(year-1900,0,1);
				String line;
				while((line=reader.readLine())!=null){
					if(line.trim().length()>0){
						String[] nameCode = line.split(";");
						context.addCountry(loadCountry(nameCode[1], nameCode[0] , new Language( this.findLocaleForCountry(nameCode[1]).getLanguage()) ), date);
					}
				}
				reader.close();

			} catch (NumberFormatException e){
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private Country loadCountry(String isoCode, String name, Language language) throws IOException{
		ISOCountry country = new ISOCountry(isoCode,name,language);
		// read countryDivisions
		Map<String ,ISOCountryDivision > divisions = new TreeMap<String ,ISOCountryDivision>();
		InputStream in = locate("iso3166-2-"  + isoCode + ".csv");
		if (in!=null){

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while((line=reader.readLine())!=null){
				if(line.trim().length()>0){
					String[] nameCode = line.split(";");
					ISOCountryDivision div = new ISOCountryDivision (country, nameCode[1], nameCode[2]);
					country.children.put(div.getDesignation(),div);
					divisions.put(div.getDesignation(), div);
				}
			}
			reader.close();
		} 
		
	
		// read cities
		boolean hasDivisions = !divisions.isEmpty();
		
	    in = locate("unlocode-"  + isoCode + ".csv");
		if (in!=null){

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while((line=reader.readLine())!=null){
				if(line.trim().length()>0){
					String[] nameCode = line.split(";");
					
					if (nameCode.length>4 && !nameCode[4].isEmpty()){
						if (hasDivisions){
							ISOCountryDivision division = divisions.get(nameCode[4]);
							if (division!=null){
								division.children.put (nameCode[1] , new UNTown (division, nameCode[1], nameCode[2]));
							} else {
								// TODO log
								System.out.println("Division " + nameCode[4] + " for country " + isoCode + " was not found");
							}
						} else {
							country.children.put( nameCode[1] , new UNTown (country, nameCode[1], nameCode[2]));
						}
					}
				}
			}
			reader.close();
		} 
		
		if (country.children.isEmpty()){
			country.children = Collections.emptyMap();
		}
		return country;
	}
	


	private static class ISOCountry extends Country{

		Map<String,AtlasLocale> children = new HashMap<String,AtlasLocale>();
		ISOCountry(String isoCode, String name, Language language) {
			super(isoCode, name,language);
		}

		@Override
		public Collection<AtlasLocale> getChildren() {
			return children.values();
		}

		@Override
		public AtlasLocale getChild(String designation) {
			return children.get(designation);
		}

	}

	private static class ISOCountryDivision extends CountryDivision{

		Map<String,AtlasLocale> children = new HashMap<String,AtlasLocale>();
		
		ISOCountryDivision(Country country, String isoCode, String name) {
			super(country, isoCode, name);
		}

		@Override
		public Collection<AtlasLocale> getChildren() {
			return children.values();
		}

		@Override
		public AtlasLocale getChild(String designation) {
			return children.get(designation);
		}


		
	}
	
	private static class UNTown extends Town{


		UNTown(AtlasLocale parent, String isoCode, String name) {
			super(parent, isoCode, name);
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
