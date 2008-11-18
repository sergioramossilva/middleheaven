package org.middleheaven.global;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Locale;

import org.middleheaven.global.atlas.Atlas;
import org.middleheaven.global.atlas.Country;
import org.middleheaven.util.measure.time.Chonologies;
import org.middleheaven.util.measure.time.Chronology;

public final class Culture implements Serializable{

	private static final long serialVersionUID = 518771233494907193L;

	public static Culture valueOf(String language){
		return new Culture(language.toLowerCase());
	}
	
	public static Culture valueOf(String language, String country){
		return new Culture(language.toLowerCase(), country.toUpperCase());
	}
	
	public static Culture valueOf(String ... parts){
		String[] variant = Arrays.copyOfRange(parts, 2, parts.length);
		return new Culture(parts[0].toLowerCase(),parts[1].toUpperCase(), variant);
	}
	
	public static Culture valueOf(CharSequence charSequence){
		if (charSequence.length()==0){
			throw new IllegalArgumentException("Char sequence is empty");
		}
		
		String[] parts = charSequence.toString().split("_");
		
		if (parts.length==1){
			return valueOf(parts[0]);
		} else if (parts.length==2){
			return valueOf(parts[0].toLowerCase(),parts[1].toUpperCase());
		} else {
			return valueOf(parts);
		}
	}
	
	
	private String country;
	private String language;
	private String[] variant = new String[0];
	
	private Culture(String languageCode){
		this(languageCode,"");
	}
	
	private Culture(String languageCode, String countryCode){
		this(languageCode,countryCode,new String[0]);
	}

	private Culture(String languageCode, String countryCode, String ... variant){
		this.variant = variant;
		this.country = countryCode;
		this.language = languageCode;
	}
	
	public Country getCountry(){
		return country==null ? null : Atlas.getCountry(country);
	}
	
	public Language getLanguage(){
		return new Language(language);
	}
	
	public Chronology getChonology(){
		return Chonologies.getChonology(this);
	}
	
	public Locale toLocale(){
		return new Locale(language,country,variant[0]);
	}
	
	
	public boolean equals(Object other){
		return other instanceof Culture && 
		((Culture)other).language.equals(this.language) &&
		((Culture)other).country.equals(this.country) && 
		Arrays.equals(this.variant, ((Culture)other).variant);
	}
	
	public int hashCode(){
		return this.language.hashCode();
	}
	
	public String toString(){
		return language + "_" + country;
	}
}
