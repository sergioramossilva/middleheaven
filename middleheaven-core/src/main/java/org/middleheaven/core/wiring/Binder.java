package org.middleheaven.core.wiring;



public interface Binder {


	public <T> BindingBuilder<T> bind(Class<T> type);
	
	public <T> PropertyBindingBuilder<T> bindProperty(Class<T> type);

}
