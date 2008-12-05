package org.middleheaven.global.atlas;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

import org.middleheaven.global.Culture;
import org.middleheaven.global.Language;
import org.middleheaven.global.address.AddressModel;
import org.middleheaven.global.address.DefaultAddressModel;
import org.middleheaven.util.measure.money.Currency;
import org.middleheaven.util.measure.money.ISOCurrency;

public abstract class Country extends AbstractAtlasLocale implements Serializable {

	private String name;
	private List<Language> languages = Collections.emptyList();

	protected Country(String isoCode){
		super(null,isoCode,isoCode);
	}
	
	protected Country(String isoCode, String name){
		super(null,isoCode,name);
	}
	
	public String toString(){
		return this.name;
	}

	@Override
	public boolean isCountry() {
		return true;
	}

	@Override
	public boolean isDivision() {
		return false;
	}

	@Override
	public boolean isTown() {
		return false;
	}
	
	public Currency getCurrentCurrency (){
		return new ISOCurrency(java.util.Currency.getInstance(new Locale(getLanguage().toString(),this.ISOCode())));
	}

	public Language getLanguage(){
		return languages.get(0);
	}

	public List<Language> getLanguages(){
		return languages;
	}

	protected void addLanguage(Language language){
		if (languages.isEmpty()){
			languages= new CopyOnWriteArrayList<Language>();
		}
		if (!this.languages.contains(language)){
			this.languages.add(language);
		}

	}
	
	public Culture getCulture(){
		return Culture.valueOf(getLanguage().toString(),this.ISOCode());
	}

	public final AddressModel getAddressModel(){
		return DefaultAddressModel.getInstance();
		/*
		try {
			AddressModelService service = ServiceRegistry.getService(AddressModel.class);
			return service.getAddressModel(this);
		} catch (ServiceNorFoundException e ){
			return DefaultAddressModel.getInstance();
		}
		*/
	}


	protected final void setName(String name){
		this.name = name;
	}


	@Override
	public final String getName() {
		return this.ISOCode();
	}

	@Override
	public final AtlasLocale getParent() {
		return Atlas.ATLAS;
	}

	@Override
	public final String getQualifiedDesignation() {
		return Atlas.ATLAS.getQualifiedDesignation() + "." + ISOCode();
	}

	public boolean equals(Object other){
		return other instanceof Country && equals((Country)other);
	}

	public boolean equals(Country other){
		return this.ISOCode().equals(other.ISOCode()); 
	}

	public int hashCode(){
		return this.ISOCode().hashCode();
	}
}
