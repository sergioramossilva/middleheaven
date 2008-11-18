package org.middleheaven.global.atlas.modules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.global.atlas.AtlasContext;
import org.middleheaven.global.atlas.AtlasLocaleInfo;
import org.middleheaven.global.atlas.AtlasModule;
import org.middleheaven.global.atlas.CountryInfo;
import org.middleheaven.util.measure.time.CalendarDate;
import org.middleheaven.util.measure.time.DateHolder;

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
				DateHolder date = CalendarDate.date(year,1,1);
				String line;
				while((line=reader.readLine())!=null){
					if(line.trim().length()>0){
						String[] nameCode = line.split(";");
						CountryInfo info = new CountryInfo(nameCode[1],date);
						info.setName(nameCode[0]);
						
						context.addCountryInfo(loadCountry(info));

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

	private CountryInfo loadCountry(CountryInfo countryInfo) throws IOException{
		
		countryInfo.addLanguage( this.findLocaleForCountry(countryInfo.getIsoCode()).getLanguage());
		
		// read countryDivisions
		Map<String ,AtlasLocaleInfo > divisions = new TreeMap<String ,AtlasLocaleInfo>();
		InputStream in = locate("iso3166-2-"  + countryInfo.getIsoCode() + ".csv");
		if (in!=null){

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while((line=reader.readLine())!=null){
				if(line.trim().length()>0){
					String[] nameCode = line.split(";");
					AtlasLocaleInfo div = new AtlasLocaleInfo (nameCode[1],countryInfo);
					div.setName(nameCode[2]);
					
					countryInfo.addAtlasLocale(div);
					divisions.put(div.getIsoCode(), div);
				}
			}
			reader.close();
		} 
		
	
		// read cities
		boolean hasDivisions = !divisions.isEmpty();
		
	    in = locate("unlocode-"  + countryInfo.getIsoCode() + ".csv");
		if (in!=null){

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while((line=reader.readLine())!=null){
				if(line.trim().length()>0){
					String[] nameCode = line.split(";");
					
					if (nameCode.length>4 && !nameCode[4].isEmpty()){
						if (hasDivisions){
							AtlasLocaleInfo division = divisions.get(nameCode[4]);
							if (division!=null){
								division.addAtlasLocale(new AtlasLocaleInfo(nameCode[1],division).setName(nameCode[2]));
							} else {
								// TODO log
								System.out.println("Division " + nameCode[4] + " for country " + countryInfo.getIsoCode() + " was not found");
							}
						} else {
							countryInfo.addAtlasLocale(new AtlasLocaleInfo(nameCode[1],countryInfo).setName(nameCode[2]));
						}
					}
				}
			}
			reader.close();
		} 
		
		return countryInfo;
	}



	

}
