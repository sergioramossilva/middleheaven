package org.middleheaven.storage;

import java.lang.reflect.InvocationTargetException;

import org.middleheaven.core.reflection.ReflectionException;
import org.middleheaven.core.reflection.inspection.Introspector;

public class StorableEnumUtils {

	
	private StorableEnumUtils(){}
	
	
	public static <T, E extends StorableEnum<T>> E valueForId(Class<E> type , T id ){
		
		try {

			@SuppressWarnings("unchecked") E[] values = (E[]) Introspector.of(type)
			.inspect()
			.staticMethods()
			.named("values")
			.retrive()
			.invoke(null, new Object[0]);
			
			for (E s : values){
				if(s.getStorableValue().equals(id)){
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
