package org.middleheaven.global.atlas;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class AtlasLocaleInfo {

	
	private String name;
	private String isoCode;
	private AtlasLocaleInfo parent;
	List<AtlasLocaleInfo> atlasLocations = new LinkedList<AtlasLocaleInfo>();
	
	public AtlasLocaleInfo( String isoCode,AtlasLocaleInfo parent) {
	
		this.isoCode = isoCode;
		this.parent = parent;
	}
	
	public Collection<AtlasLocaleInfo> getCildrenAtlasLocations() {
		return atlasLocations;
	}
	
	public void addAtlasLocale(AtlasLocaleInfo child) {
		this.atlasLocations.add(child);
	}

	
	public String getName() {
		return name;
	}
	public String getIsoCode() {
		return isoCode;
	}
	public AtlasLocaleInfo getParent() {
		return parent;
	}

	public AtlasLocaleInfo setName(String name) {
		this.name = name;
		return this;
	}
	
	public boolean equals(Object other) {
		return other instanceof AtlasLocaleInfo
				&& equals((AtlasLocaleInfo) other);
	}

	public boolean equals(AtlasLocaleInfo other) {
		return other.isoCode.equals(other.isoCode);
	}

	public int hashCode() {
		return isoCode.hashCode();
	}
}
