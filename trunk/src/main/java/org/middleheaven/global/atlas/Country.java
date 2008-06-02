package org.middleheaven.global.atlas;

import java.io.Serializable;
import java.util.Currency;
import java.util.Locale;

import org.middleheaven.global.address.AddressModel;
import org.middleheaven.global.address.DefaultAddressModel;


public abstract class Country implements AtlasLocale, Serializable {

	private String isoCode;
	private String name;
	private AddressModel addressModel;
	private Language language;
	
    protected Country(String isoCode,String name,Language language){
		this.isoCode = isoCode;
		this.name = name;
		this.language = language;
		try {
			addressModel = (AddressModel)Class.forName("org.middleheaven.global.address.models.AddresModel" + isoCode.toUpperCase()).newInstance();
		} catch (Exception e) {
			addressModel = DefaultAddressModel.getInstance(); 
		} 

	}
    
    public Currency getCurrentCurrency (){
    	return Currency.getInstance(new Locale(this.language.toString(),this.isoCode));
    }
    
    public Language getLanguage(){
    	return language;
    }
	
    protected void setLanguage(Language language){
    	this.language = language;
    }
    
    public Locale toLocale(){
    	return new Locale(language.toString(),isoCode);
    }
    public final AddressModel getAddressModel(){
    	return addressModel;
    }
    
    protected void setAddressModel(AddressModel addressModel){
    	this.addressModel = addressModel;
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
