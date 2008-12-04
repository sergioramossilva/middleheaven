package org.middleheaven.global.atlas;

import java.io.Serializable;

public abstract class Town  extends AbstractAtlasLocale implements Serializable{

	AtlasLocale parent;
	
	protected Town(AtlasLocale parent , String isoCode,String name){
		super(parent,isoCode,name);
	}

	
	public boolean equals(Object other){
		return other instanceof Town && equals((Town)other);
	}
	
	public boolean equals(Town other){
		return this.ISOCode().equals(other.ISOCode()); 
	}
	
	@Override
	public boolean isCountry() {
		return false;
	}

	@Override
	public boolean isDivision() {
		return false;
	}

	@Override
	public boolean isTown() {
		return true;
	}



}
