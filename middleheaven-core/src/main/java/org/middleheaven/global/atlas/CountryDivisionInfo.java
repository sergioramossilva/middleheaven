package org.middleheaven.global.atlas;

public class CountryDivisionInfo extends AtlasLocaleInfo{

	private CountryDivisionType type;
	
	public CountryDivisionType getType() {
		return type;
	}

	public void setType(CountryDivisionType type) {
		this.type = type;
	}

	public CountryDivisionInfo(String isoCode, AtlasLocaleInfo parent) {
		super(isoCode, parent);
	}

	
}
