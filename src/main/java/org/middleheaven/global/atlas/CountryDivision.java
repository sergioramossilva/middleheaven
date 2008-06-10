package org.middleheaven.global.atlas;

import java.io.Serializable;

public abstract class CountryDivision implements AtlasLocale , Serializable{

	private String isoCode;
	private String name;
	private Country country;

	protected CountryDivision(Country country, String isoCode,String name){
		this.isoCode = isoCode;
		this.name = name;
		this.country = country;
	}
    
	public String ISOCode(){
		return country.ISOCode() + " "+ isoCode;
	}
	
	public String subDevision(){
		return isoCode;
	}
	
	public final String getName(){
		return name;
	}

	@Override
	public String getDesignation() {
		return isoCode;
	}

	@Override
	public AtlasLocale getParent() {
		return country;
	}

	@Override
	public String getQualifiedDesignation() {
		return country.getQualifiedDesignation() + "." + isoCode;
	}

	public final String toString(){
		return getDesignation();
	}
	
	public boolean equals(Object other){
		return other instanceof CountryDivision && equals((CountryDivision)other);
	}
	
	public boolean equals(CountryDivision other){
		return this.isoCode.equals(other.getDesignation()) && this.country.equals(other.country); 
	}
	
	public int hashCode(){
		return this.isoCode.hashCode() ^ this.country.hashCode();
	}
}
