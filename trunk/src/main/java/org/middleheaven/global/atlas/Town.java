package org.middleheaven.global.atlas;

import java.io.Serializable;

public abstract class Town  extends AbstractAtlasLocale implements Serializable{


	private String name;
	AtlasLocale parent;
	
	protected Town(AtlasLocale parent , String isoCode,String name){
		super(parent,isoCode);
		this.name = name;
	}
	
    public final String getName(){
    	return name;
    }
	
	public boolean equals(Object other){
		return other instanceof Town && equals((Town)other);
	}
	
	public boolean equals(Town other){
		return this.ISOCode().equals(other.ISOCode()); 
	}
	

}
