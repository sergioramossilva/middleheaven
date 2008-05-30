package org.middleheaven.global.atlas;

import java.io.Serializable;


public abstract class Country implements AtlasLocale, Serializable {

	
	private String isoCode;
	private String name;
	
    protected Country(String isoCode,String name){
		this.isoCode = isoCode;
		this.name = name;
	}
	
    public final String getName(){
    	return name;
    }
    
    public final String ISOCode(){
    	return isoCode;
    }
    
	@Override
	public final String getDesignation() {
		return isoCode;
	}

	@Override
	public final AtlasLocale getParent() {
		return Atlas.ATLAS;
	}

	@Override
	public final String getQualifiedDesignation() {
		return Atlas.ATLAS.getQualifiedDesignation() + "." + isoCode;
	}

	public final String toString(){
		return isoCode;
	}
	
	public boolean equals(Object other){
		return other instanceof Country && equals((Country)other);
	}
	
	public boolean equals(Country other){
		return this.isoCode.equals(other.getDesignation()); 
	}
	
	public int hashCode(){
		return this.isoCode.hashCode();
	}
}
