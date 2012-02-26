package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;

public class ResolverBindingBuilder<T> {

	protected Binding binding;
	protected EditableBinder binder;

	ResolverBindingBuilder (EditableBinder binder , Binding binding){
		this.binder = binder;
		this.binding = binding;
		binder.addBinding(binding);
	}
	
	public ResolverBindingBuilder<T> in(Class<? extends Annotation> scope){
		return this;
	}
}
