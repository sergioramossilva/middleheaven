package org.middleheaven.core.wiring;


interface EditableBinder extends Binder{

	public BeanModel getBeanModel(Class<?> type);
	public BeanModel getBeanModel(Object instance);
	
	public <T> T getInstance(WiringSpecification<T> query);
	
	public void addBinding(Binding binding);
	public void removeBinding(Binding binding);
	
	public void addBindings(BeanModel model, Resolver<?> resolver );
	
	public ScopePool getScopePool(Binding binding);

}
