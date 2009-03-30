package org.middleheaven.core.wiring;
import java.lang.annotation.Annotation;

import org.middleheaven.core.reflection.ReflectionUtils;

public class BindingBuilder<T> {

	protected Binding binding;
	protected EditableBinder binder;
	
	BindingBuilder (EditableBinder binder , Class<T> type){
		this.binder = binder;
		this.binding = new Binding();
		binding.setStartType(type);
		binder.addBinding(binding);
	
	}
	
	@SuppressWarnings("unchecked")
	public BindingBuilder<T> to(Class<? extends T> type){
		binding.setResolver(new DefaultResolver(type,binder));
		return this;
	}
	
	public BindingBuilder<T> in(Class<? extends Annotation> scope){
		if (!ReflectionUtils.isAnnotadedWith(scope, ScopeSpecification.class)){
			throw new IllegalArgumentException(scope.getName() + " is not a " + ScopeSpecification.class.getName());
		}
		binding.setTargetScope(scope);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public BindingBuilder<T> toInstance(T object){
		binding.setResolver(new InstanceResolver(object));
		return this;
	} 
	
	public BindingBuilder<T> toProvider(Class<Provider<T>> type){
		//binding.setResolver(new ProviderResolver(type));
		return this;
	}
	
	public BindingBuilder<T> annotatedWith(Class<? extends BindingSpecification> type){
		binder.removeBinding(binding);
		binding.addAnnotation(type);
		binder.addBinding(binding);
		return this;
	}
	

}
