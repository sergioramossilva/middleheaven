package org.middleheaven.core.wiring;


interface EditableBinder {

	
	public void addBinding(Binding binding);
	public <T> T getInstance(WiringSpecification<T> query);
	public void removeBinding(Binding binding);

}
