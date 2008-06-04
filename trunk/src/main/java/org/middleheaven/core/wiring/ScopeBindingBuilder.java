package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;

import org.middleheaven.core.wiring.service.Service;

public class ScopeBindingBuilder<T> {

	protected Binding<T> binding;
	protected EditableBinder binder;

	
	ScopeBindingBuilder (EditableBinder binder , Binding<T> binding){
		this.binder = binder;
		this.binding = binding;
	}
	
	public ScopeBindingBuilder<T> to(Class<? extends Annotation> scopeClass){
		binder.removeBinding(binding);
		this.binding.setStartType(scopeClass);
		binder.addBinding(binding);
		return this;
	}

}
