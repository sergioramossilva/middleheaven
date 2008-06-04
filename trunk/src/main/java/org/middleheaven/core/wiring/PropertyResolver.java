package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class PropertyResolver<T> implements Resolver<T> {

	Map<String, Object > map = new TreeMap<String, Object >();
	
	@Override
	public T resolve(Class<T> type, Set<Annotation> annotations) {

		for (Annotation a : annotations){
			if (Property.class.isAssignableFrom(a.annotationType())){
				Object obj = map.get(((Property)a).value());
				if (type.isAssignableFrom(obj.getClass())){
					return type.cast(obj);
				} else {
					throw new ClassCastException("Impossible to convert " + obj.getClass().getName() + " to " + type.getName());
				}
				
			}
		}
		throw new BindingException("Cannot resolver " + type.getName());
	}
	
	public void setProperty(String name, Object value){
		map.put(name, value);
	}

}
