package org.middleheaven.core.wiring;


interface EditableBinder {

	
	public <T> void addBinding(Binding<T> binding);
	public <T> T getInstance(WiringSpecification<T> query);
	public <T> void removeBinding(Binding<T> binding);

}
