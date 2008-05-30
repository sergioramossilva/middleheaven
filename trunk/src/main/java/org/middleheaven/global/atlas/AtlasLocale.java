package org.middleheaven.global.atlas;

import java.util.Collection;

public interface AtlasLocale {

	public String getDesignation();
	public String getQualifiedDesignation();
	public AtlasLocale getParent();
	public Collection<AtlasLocale> getChildren(); 
	public AtlasLocale getChild(String designation); 
}
