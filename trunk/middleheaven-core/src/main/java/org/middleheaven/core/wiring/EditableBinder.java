package org.middleheaven.core.wiring;


interface EditableBinder extends Binder{

	public BeanDependencyModel getBeanModel(Class<?> type);
	public BeanDependencyModel getBeanModel(Object instance);
	
	public Object getInstance(WiringQuery query);
	
	public void addBinding(Binding binding);
	public void removeBinding(Binding binding);
	
	public void addBindings(BeanDependencyModel model, Resolver resolver );
	

}
