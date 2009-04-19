package org.middleheaven.core.wiring;

import java.util.HashMap;
import java.util.Map;

/**
 * Allows to retrieve instances form name-keyed , predetermined , values.
 *
 * @param <T>
 */
public class PropertyResolver<T> implements Resolver<T> {

	private final Map<String, Object > map = new HashMap<String, Object >();

	
	public PropertyResolver(){}
	
	@Override
	public T resolve(WiringSpecification<T> query) {

		String name = query.getParam("name");

		if (name!=null){
			Object obj = map.get(name);
			if (query.getContract().isAssignableFrom(obj.getClass())){
				return query.getContract().cast(obj);
			} else {
				throw new ClassCastException("Impossible to convert " + obj.getClass().getName() + " to " + query.getContract().getName());
			}
		}

		throw new CannotResolveException(query.getContract());
	}

	public void setProperty(String name, Object value){
		map.put(name, value);
	}

}
