package org.middleheaven.global.phone;

import java.util.EnumMap;
import java.util.Map;

import org.middleheaven.global.address.AddressPartType;
import org.middleheaven.global.atlas.Country;

public abstract class PhoneNumber {

	private Country country;
	private Map<PhonePartType, String> parts = new EnumMap<PhonePartType, String>(PhonePartType.class);
	
	
	public Country getCountry(){
		return country;
	}
	
	public void setPhonePart(PhonePartType type, String part){
		if (type.equals(PhonePartType.COUNTRY)){
			throw new IllegalArgumentException( type + " cannot be changed");
		}
		parts.put(type, part);
	}
	
	public void clearPhonePartType(PhonePartType type){
		parts.remove(type);
	}
	
	public void clearParts(){
		parts.clear();
	}
}
