package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;

public class ResolverBindingBuilder<T> {

	protected Binding<T> binding;
	protected EditableBinder binder;

	ResolverBindingBuilder (EditableBinder binder , Binding<T> binding){
		this.binder = binder;
		this.binding = binding;
		this.binding.addAnnotation(Property.class);
		binder.addBinding(binding);
	}
	
	public ResolverBindingBuilder in(Class<? extends Annotation> scope){
		
		
		return this;
	}
}
