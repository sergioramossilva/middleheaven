package org.middleheaven.global.atlas;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class Atlas implements AtlasLocale{

	public static final Atlas ATLAS = new Atlas();

	private Atlas(){}

	/**
	 * Returns all current countries in the World
	 */
	@Override
	public Set<AtlasLocale> getChildren() {
		AtlasService service = null;
		return Collections.unmodifiableSet(new HashSet<AtlasLocale>(service.findALLCountries()));
	}

	@Override
	public String getDesignation() {
		return "atlas";
	}

	@Override
	public AtlasLocale getParent() {
		return null;
	}

	@Override
	public String getQualifiedDesignation() {
		return "atlas";
	}

	@Override
	public AtlasLocale getChild(String designation) {
		AtlasService service = null;
		return service.findCountry(designation);
	}
}
