package org.middleheaven.core.wiring;


interface EditableBinder extends Binder{

	public WiringModel getWiringModel(Class<?> type);
	public <T> T getInstance(WiringSpecification<T> query);
	
	public void addBinding(Binding binding);
	public void removeBinding(Binding binding);
	
	public ScopePool getScopePool(Binding binding);
	


}
