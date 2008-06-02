package org.middleheaven.global.address;

import java.util.EnumMap;
import java.util.Map;

import org.middleheaven.global.atlas.Country;

public abstract class Address {

	private Country country;
	private PostalCode postalCode;
	private Map<AddressPartType, String> parts = new EnumMap<AddressPartType, String>(AddressPartType.class);
	
	public Country getCountry(){
		return country;
	}
	
	public PostalCode getPostalCode(){
		return postalCode;
	}
	
	public void setAddressPart(AddressPartType type, String part){
		parts.put(type, part);
	}
	
	public void clearAddressPart(AddressPartType type){
		parts.remove(type);
	}
	
	public void clearParts(){
		parts.clear();
	}
	
	public String getAddressPart(AddressPartType type){
		return parts.get(type);
	}
	
    public boolean equals(Object other){
		return other instanceof Address && equals((Address)other);
	}
	
	public boolean equals(Address other){
		return this.country.equals(other.country) && this.postalCode.equals(other.postalCode)
		 && this.parts.equals(other.parts);
	}
	
	public int hashCode(){
		return this.country.hashCode() ^ this.postalCode.hashCode();
	}
}
