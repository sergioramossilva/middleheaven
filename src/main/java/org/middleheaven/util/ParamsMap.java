package org.middleheaven.util;

import java.util.TreeMap;

public final class ParamsMap extends TreeMap<String,String> {

	
	public ParamsMap setParam(String name, String value){
		this.put(name, value);
		return this;
	}
}
