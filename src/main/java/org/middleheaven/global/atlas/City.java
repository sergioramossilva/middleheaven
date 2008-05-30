package org.middleheaven.global.atlas;

import java.io.Serializable;

public abstract class City  implements AtlasLocale, Serializable{


	private String isoCode;
	private String name;
	AtlasLocale parent;
	
	protected City(AtlasLocale parent , String isoCode,String name){
		this.isoCode = isoCode;
		this.name = name;
		this.parent = parent;
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
		return parent;
	}

	@Override
	public final String getQualifiedDesignation() {
		return this.getParent().getQualifiedDesignation() + "." + isoCode;
	}

	public final String toString(){
		return isoCode;
	}
	
	public boolean equals(Object other){
		return other instanceof City && equals((City)other);
	}
	
	public boolean equals(City other){
		return this.isoCode.equals(other.getDesignation()); 
	}
	
	public int hashCode(){
		return this.isoCode.hashCode();
	}
}
