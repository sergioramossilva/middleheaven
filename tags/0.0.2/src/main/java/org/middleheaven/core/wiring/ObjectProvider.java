package org.middleheaven.core.wiring;

public class ObjectProvider<T> implements Provider<T>{

	private T obj;
	
	public ObjectProvider(T obj) {
		this.obj = obj;
	}

	@Override
	public T provide() {
		return obj;
	}

}
