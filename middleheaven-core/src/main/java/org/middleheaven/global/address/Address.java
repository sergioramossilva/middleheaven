package org.middleheaven.global.address;

import java.util.EnumMap;
import java.util.Map;

import org.middleheaven.global.atlas.Country;

public abstract class Address {

	private Country country;
	private PostalCode postalCode;
	private Map<AddressPartType, String> parts = new EnumMap<AddressPartType, String>(AddressPartType.class);
	
	
	public Address (){
		
	}
	
	public Country getCountry(){
		return country;
	}
	
	public PostalCode getPostalCode(){
		return postalCode;
	}
	
	/**
	 * Attributes {@link Country}.
	 * @param country the country to set
	 */
	protected void setCountry(Country country) {
		this.country = country;
		parts.put(AddressPartType.COUNTRY, country.toString());
	}

	/**
	 * Attributes {@link PostalCode}.
	 * @param postalCode the postalCode to set
	 */
	protected void setPostalCode(PostalCode postalCode) {
		this.postalCode = postalCode;
		parts.put(AddressPartType.POSTAL_CODE, postalCode.toString());
	}

	public void setAddressPart(AddressPartType type, String part){
		if (type.equals(AddressPartType.COUNTRY) || type.equals(AddressPartType.COUNTRY_CODE)|| type.equals(AddressPartType.POSTAL_CODE)){
			throw new IllegalArgumentException( type + " cannot be changed");
		}
		parts.put(type, part);
		
	}
	
	public void clearAddressPart(AddressPartType type){
		parts.remove(type);
	}
	
	public void clearParts(){
		parts.clear();
	}
	
	public String getAddressPart(AddressPartType type){
		if (type.equals(AddressPartType.COUNTRY)){
			return country.getName();
		} else if (type.equals(AddressPartType.POSTAL_CODE)){
			return postalCode.toString();
		} else if (type.equals(AddressPartType.COUNTRY_CODE)){
			return country.ISOCode();
		} else {
			return parts.get(type);
		}
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
