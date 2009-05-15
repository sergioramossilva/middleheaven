package org.middleheaven.util.collections;

import java.util.HashMap;

public final class ParamsMap extends EnhancedMapAdapter<String,String> {

	private static final long serialVersionUID = -6830223903725787564L;

	public ParamsMap() {
		super(new HashMap<String,String>());
	}

	public ParamsMap setParam(String name, String value){
		this.put(name, value);
		return this;
	}
}
