package org.middleheaven.global.atlas;

import java.util.Collection;

public abstract class AbstractAtlasLocale implements AtlasLocale {

	private String isoCode;
	private AtlasLocale parent;
	protected String name;

    
	protected AbstractAtlasLocale(AtlasLocale parent,  String isoCode , String name){
		this.isoCode = isoCode;
		this.parent = parent;
		this.name = name;
	}
    
	
	public String ISOCode(){
		return isoCode;
	}
	
	public String subDevision(){
		return isoCode;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public AtlasLocale getParent() {
		return parent;
	}

	@Override
	public String getQualifiedDesignation() {
		return parent.getQualifiedDesignation() + "." + isoCode;
	}
	
	public boolean equals(Object other){
		return other instanceof AtlasLocale && equals((AtlasLocale)other);
	}
	
	public boolean equals(AtlasLocale other){
		return this.isoCode.equals(other.getName()) && this.parent.equals(other.getParent()); 
	}
	
	public int hashCode(){
		return this.isoCode.hashCode() ^ this.parent.hashCode();
	}

	public String toString(){
		return name;
	}
	
	@Override
	public abstract AtlasLocale getChild(String designation);

	@Override
	public abstract Collection<AtlasLocale> getChildren() ;
}
