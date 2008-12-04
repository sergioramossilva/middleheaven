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

import org.middleheaven.global.atlas.Atlas;
import org.middleheaven.global.atlas.AtlasContext;
import org.middleheaven.global.atlas.AtlasLocaleInfo;
import org.middleheaven.global.atlas.AtlasModule;
import org.middleheaven.global.atlas.CountryDivisionInfo;
import org.middleheaven.global.atlas.CountryDivisionType;
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
			String name = "/" + Atlas.class.getPackage().getName().replaceAll("\\.","/") + "/data/" + filename;
			return this.getClass().getResourceAsStream(name);
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
		} else {
			throw new AtlasNotFoundException();
		}
	}



	private CountryInfo loadCountry(CountryInfo countryInfo) throws IOException{

		countryInfo.addLanguage( this.findLocaleForCountry(countryInfo.getIsoCode()).getLanguage());

		// read countryDivisions
		Map<String ,CountryDivisionInfo > divisions = new TreeMap<String ,CountryDivisionInfo>();
		InputStream in = locate("iso3166-2-"  + countryInfo.getIsoCode() + ".csv");
		if (in!=null){

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while((line=reader.readLine())!=null){
				if(line.trim().length()>0){
					String[] nameCode = line.split(";");
					CountryDivisionInfo div = new CountryDivisionInfo (nameCode[1],countryInfo);
					div.setName(nameCode[2]);
					div.setType(CountryDivisionType.fromText(nameCode[3]));
					countryInfo.addAtlasLocale(div);
					divisions.put(div.getIsoCode(), div);
				}
			}
			reader.close();
		} 

		// read cities

		in = locate("unlocode-"  + countryInfo.getIsoCode() + ".csv");
		if (in!=null){

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while((line=reader.readLine())!=null){
				if(line.trim().length()>0){
					String[] nameCode = line.split(";");
					if (nameCode.length>4){

						if (divisions.isEmpty()) {
							countryInfo.addAtlasLocale(new AtlasLocaleInfo(nameCode[1],countryInfo).setName(nameCode[2]));
						}  else if (nameCode[4].trim().length()>0 ){ // division is defined
							CountryDivisionInfo division = divisions.get(nameCode[4]);
							if (division!=null){
								division.addAtlasLocale(new AtlasLocaleInfo(nameCode[1],division).setName(nameCode[2]));
							} else {
								// division not found
								division = new CountryDivisionInfo (nameCode[4],countryInfo);
								division.setName(nameCode[2]);
								division.setType(CountryDivisionType.fromText(nameCode[3]));

								countryInfo.addAtlasLocale(division);
								divisions.put(division.getIsoCode(), division);
								division.addAtlasLocale(new AtlasLocaleInfo(nameCode[1],division).setName(nameCode[2]));

							}


						} else {
							System.out.println("Cannot add a non division to a devided country (division=" + nameCode[2] + ", country=" +countryInfo.getIsoCode() + ")");
						}
					}

				}
			}
			reader.close();
		} 

		return countryInfo;
	}





}
