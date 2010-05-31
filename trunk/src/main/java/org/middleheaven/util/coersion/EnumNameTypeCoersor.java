package org.middleheaven.util.coersion;

import java.lang.reflect.InvocationTargetException;

import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.core.reflection.ReflectionException;

public class EnumNameTypeCoersor<E extends Enum> extends AbstractTypeCoersor<String, E> {

	@Override
	public <T extends E> T coerceForward(String value, Class<T> type) {
		try {
			if(value ==null){
				return null;
			}
			return type.cast(Introspector.of(type)
					.inspect().staticMethods()
					.named("valueOf").withParametersType(new Class<?>[]{String.class})
					.retrive()
					.invoke(null, new Object[]{value}));
			
		} catch (IllegalArgumentException e) {
			throw ReflectionException.manage(e, type);
		} catch (IllegalAccessException e) {
			throw ReflectionException.manage(e, type);
		} catch (InvocationTargetException e) {
			throw ReflectionException.manage(e, type);
		}
	}

	@Override
	public <T extends String> T coerceReverse(E value, Class<T> type) {
		return  value == null ? null : (T) value.name();
	}





}
