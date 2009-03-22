package org.middleheaven.ui;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.global.Culture;
import org.middleheaven.util.conversion.TypeConvertions;

public class MapContext extends AbstractContext {

	EnumMap<ContextScope, ParamMap> contextMap = new EnumMap<ContextScope, ParamMap>(ContextScope.class);
	private Culture culture;

	static class ParamMap extends TreeMap<String, Object>{
		
	}
	
	public MapContext(Culture culture){
		this.culture = culture;
		for ( ContextScope scope : ContextScope.values()){
			contextMap.put(scope, new ParamMap());
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
	

	public <O> Map<String, O> getScopeMap(ContextScope scope, Class<O> type) {
		
		ParamMap map = contextMap.get(scope);
		if ( map == null){
			return Collections.emptyMap();
		}
		Map<String,O> result = new TreeMap<String,O>();
		for (Map.Entry<String,Object> entry : map.entrySet()){
			result.put(entry.getKey(), TypeConvertions.convert(entry.getValue(), type));
		}
		return result;
	}

	@Override
	public Culture getCulture() {
		return culture;
	}

}
