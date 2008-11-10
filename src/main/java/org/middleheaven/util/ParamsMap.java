package org.middleheaven.util;

import java.util.TreeMap;

public final class ParamsMap extends TreeMap<String,String> {

	
	private static final long serialVersionUID = -6830223903725787564L;

	public ParamsMap setParam(String name, String value){
		this.put(name, value);
		return this;
	}
}
