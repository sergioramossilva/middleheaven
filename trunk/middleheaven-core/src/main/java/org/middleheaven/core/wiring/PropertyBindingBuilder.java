package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;

import org.middleheaven.core.wiring.annotations.Named;

public class PropertyBindingBuilder<T>  {

	protected Binding binding;
	protected EditableBinder binder;
	protected String label;
	protected T object;
	
	PropertyBindingBuilder (EditableBinder binder , Binding binding){
		this.binder = binder;
		this.binding = binding;
		binder.addBinding(binding);
	}

	public PropertyBindingBuilder<T> labeled (String label){
		this.label = label;
		if (this.object != null && label!=null){
			((PropertyResolver<T>)binding.getResolver()).setProperty(label , object);
		}
		return this;
	}
	
	public PropertyBindingBuilder<T> in(Class<? extends ScopePool> scope){
		binding.setTargetScope(scope);
		return this;
	}
	
	public PropertyBindingBuilder<T> toInstance(T object){
		this.object = object;
		if (this.object != null && label!=null){
			((PropertyResolver<T>)binding.getResolver()).setProperty(label , object);
		}
		return this;
	} 
	
	public PropertyBindingBuilder<T> toProvider(Class<Provider<T>> type){
		//binding.setResolver(new ProviderResolver(type));
		return this;
	}
	
//	public PropertyBindingBuilder<T> annotatedWith(Class<? extends Annotation> type){
//		binder.removeBinding(binding);
//		binding.addAnnotation(type);
//		binder.addBinding(binding);
//		return this;
//	}
}
