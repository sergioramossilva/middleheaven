package org.middleheaven.util.coersion;

import org.middleheaven.reflection.Reflector;

public class EnumNameTypeCoersor<E extends Enum> extends AbstractTypeCoersor<String, E> {

	@Override
	public <T extends E> T coerceForward(String value, Class<T> type) {
	
			if(value ==null){
				return null;
			}
			return type.cast(Reflector.getReflector().reflect(type)
					.inspect().staticMethods()
					.named("valueOf").withParametersType(new Class<?>[]{String.class})
					.retrive()
					.invokeStatic(new Object[]{value}));
			
		
	}

	@Override
	public <T extends String> T coerceReverse(E value, Class<T> type) {
		return  value == null ? null : (T) value.name();
	}





}
