package org.middleheaven.core.reflection.inspection;

import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.reflection.ProxyHandler;



public class ObjectInstrospector<T> {

	private T object;

	ObjectInstrospector(T object) {
		this.object = object;
	}
	
	public T copyTo(T copy ){

		for (PropertyAccessor fa : introspectClass().inspect().properties().retriveAll()){
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
	
	public Class<?> getRealType(){
		return Reflector.getReflector().getRealType(getType());
	}
	
	@SuppressWarnings("unchecked")
	public ClassIntrospector<T> introspectClass(){
		return (ClassIntrospector<T>) Introspector.of(object.getClass());
	}

	public Object unproxy() {
		return Reflector.getReflector().unproxy(this.object);
	}

	public <I> I newProxyInstance(ProxyHandler handler,
			Class<I> proxyInterface ,Class<?> ... adicionalInterfaces) {
		
		return Reflector.getReflector().proxyObject(this.object,handler, proxyInterface, adicionalInterfaces);
	}
	
	public <I> I newProxyInstance (Class<I> proxyInterface){
		return Reflector.getReflector().proxyObject(this.object, proxyInterface);
	}


}
