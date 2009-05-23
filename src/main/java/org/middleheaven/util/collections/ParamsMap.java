package org.middleheaven.util.collections;

import java.util.HashMap;
import java.util.Map;

public final class ParamsMap extends EnhancedMapAdapter<String,String> {

	private static final long serialVersionUID = -6830223903725787564L;

	public ParamsMap() {
		super(new HashMap<String,String>());
	}
	
	public ParamsMap(Map<String,String> other) {
		super(new HashMap<String,String>());
		this.putAll(other);
	}


	public ParamsMap setParam(String name, String value){
		this.put(name, value);
		return this;
	}
}
