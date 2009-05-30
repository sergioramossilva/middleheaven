package org.middleheaven.core.reflection;

public class ObjectInstrospector<T> {

	private T object;

	public ObjectInstrospector(T object) {
		this.object = object;
	}
	
	public T copyTo(T copy ){

		for (PropertyAccessor fa : introspectClass().properties()){
			fa.setValue(copy, fa.getValue(object));
		}
		return copy;
	}
	
	public T getInstance(){
		return object;
	}
	
	public boolean isNull(){
		return object==null;
	}
	
	@SuppressWarnings("unchecked")
	public Class<T> getType() {
		return (Class<T>) object.getClass();
	}
	
	@SuppressWarnings("unchecked")
	public ClassIntrospector<T> introspectClass(){
		return (ClassIntrospector<T>) Introspector.of(object.getClass());
	}

}
