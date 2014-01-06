package org.middleheaven.culture;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Locale;

import org.middleheaven.util.StringUtils;

/**
 * Representation of a culture.
 * A culture is identified by a language and a country and an optional set of variants
 *
 */
public final class Culture implements Serializable{
	
	private static final long serialVersionUID = 518771233494907193L;

	public static Culture defaultValue(){
		return valueOf(Locale.getDefault());
	}
	
	public static Culture valueOf(Locale locale) {
		if (locale == null){
			throw new IllegalArgumentException("Locale is required");
		}
		return valueOf(locale.getLanguage(), locale.getCountry());
	}

	public static Culture valueOf(String language, String country){
		if (StringUtils.isEmptyOrBlank(language)){
			throw new IllegalArgumentException("Language is required");
		}
		if (StringUtils.isEmptyOrBlank(country)){
			throw new IllegalArgumentException("Country is required");
		}
		return new Culture(language.trim().toLowerCase(), country.trim().toUpperCase());
	}
	
	public static Culture valueOf(String ... parts){
		String[] variant = Arrays.copyOfRange(parts, 2, parts.length);
		return new Culture(parts[0].trim().toLowerCase(),parts[1].trim().toUpperCase(), variant);
	}
	
	public static Culture valueOf(CharSequence charSequence){
		if (StringUtils.isEmptyOrBlank(charSequence)){
			throw new IllegalArgumentException("Argument cannot be null");
		}
		final String culture = charSequence.toString();
		if(culture.contains("_")){
			return new Culture(culture);
		} else {
			String[] parts = culture.split("_");
			
			if (parts.length==1){
				return new Culture(parts[0].trim());
			} else if (parts.length==2){
				return valueOf(parts[0],parts[1]);
			} else {
				return valueOf(parts);
			}
		}
		
	}
	
	
	private final String country;
	private final String language;
	private String[] variant = new String[0];
	
	private Culture(String languageCode){
		this(languageCode, "");
	}
	
	private Culture(String languageCode, String countryCode){
		this(languageCode,countryCode,new String[0]);
	}

	private Culture(String languageCode, String countryCode, String ... variant){
		this.variant = variant;
		this.country = countryCode;
		this.language = languageCode;
	}
	
	public CountryIsoCode getCountry(){
		return country.isEmpty() ? null : new CountryIsoCode(country);
	}
	
	public Language getLanguage(){
		return Language.valueOf(language);
	}
	
	public Locale toLocale(){
		if (country != null){
			if (variant.length>0){
				return new Locale(language,country,variant[0]);
			} else {
				return new Locale(language,country);
			}
		} else {
			return new Locale(language);
		}
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
