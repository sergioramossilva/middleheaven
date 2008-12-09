package org.middleheaven.ui;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.util.conversion.TypeConvertions;

public class MapContext extends AbstractContext {

	EnumMap<ContextScope, Map<String, Object>> contextMap = new EnumMap<ContextScope, Map<String, Object>>(ContextScope.class);
	
	
	public MapContext(){
		for ( ContextScope scope : ContextScope.values()){
			contextMap.put(scope, new TreeMap<String, Object>());
		}
	}
	@Override
	public <T> T getAttribute(ContextScope scope, String name, Class<T> type) {
		return TypeConvertions.convert(contextMap.get(scope).get(name), type);
	}

	@Override
	public Enumeration<String> getAttributeNames(ContextScope scope) {
		return Collections.enumeration(contextMap.get(scope).keySet());
	}

	@Override
	public void setAttribute(ContextScope scope, String name, Object value) {
		contextMap.get(scope).put(name, value);
	}

}
