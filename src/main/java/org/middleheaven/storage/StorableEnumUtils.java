package org.middleheaven.storage;

import java.lang.reflect.InvocationTargetException;

import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.core.reflection.ReflectionException;

public class StorableEnumUtils {

	
	private StorableEnumUtils(){}
	
	
	public static <E extends StorableEnum> E valueForId(Class<E> type , Integer id ){
		
		try {

			@SuppressWarnings("unchecked") E[] values = (E[]) Introspector.of(type)
			.inspect()
			.staticMethods()
			.named("values")
			.retrive()
			.invoke(null, new Object[0]);
			
			for (E s : values){
				if(s.getIdentity().equals(id)){
					return s;
				}
			}
			
			return null;
		} catch (IllegalArgumentException e) {
			throw ReflectionException.manage(e, type);
		} catch (IllegalAccessException e) {
			throw ReflectionException.manage(e, type);
		} catch (InvocationTargetException e) {
			throw ReflectionException.manage(e, type);
		}
	}
}
