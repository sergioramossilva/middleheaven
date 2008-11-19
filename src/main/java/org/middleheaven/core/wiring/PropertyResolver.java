package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.TreeMap;

/**
 * Allows to retrieve instances form name-keyed , predetermined , values.
 *
 * @param <T>
 */
public class PropertyResolver<T> implements Resolver<T> {

	Map<String, Object > map = new TreeMap<String, Object >();
	
	@Override
	public T resolve(WiringSpecification<T> query) {

		for (Annotation a : query.getSpecifications()){
			if (Property.class.isAssignableFrom(a.annotationType())){
				Object obj = map.get(((Property)a).value());
				if (query.getContract().isAssignableFrom(obj.getClass())){
					return query.getContract().cast(obj);
				} else {
					throw new ClassCastException("Impossible to convert " + obj.getClass().getName() + " to " + query.getContract().getName());
				}
				
			}
		}
		throw new CannotResolveException(query.getContract());
	}
	
	public void setProperty(String name, Object value){
		map.put(name, value);
	}

}
