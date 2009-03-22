package org.middleheaven.core.reflection;

/**
 * Facilitates reflection invocation of methods and fields.
 *
 * @param <T> the class type to inspect.
 */
public final class Reflector<T> {

	public static <O> Reflector<O> of (Class<O> type){
		return new Reflector<O>(type);
	}
	
	@SuppressWarnings("unchecked")
	public static <O>  Reflector<O>  of (O obj){
		// the class is of type O
		return (Reflector<O>) of(obj.getClass());
	}
	
	private Class<T> type;
	private Reflector(Class<T> type) {
		this.type = type;
	}
	
	public T createInstance(Object ... arguments){
		return ReflectionUtils.newInstance(type, arguments);
	}
	
	public FieldAcessor field(String fieldName){
		return ReflectionUtils.getPropertyAccessor(type, fieldName);
	}
	
}
