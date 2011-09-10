package org.middleheaven.core.wiring;
import java.lang.annotation.Annotation;

import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.wiring.annotations.BindingSpecification;
import org.middleheaven.core.wiring.annotations.ScopeSpecification;

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
		binding.setResolver(FactoryResolver.instanceFor(type,binder));
		return this;
	}
	
	public BindingBuilder<T> in(Class<? extends Annotation> scope){
		if (!Introspector.of(scope).isAnnotadedWith(ScopeSpecification.class)){
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
	
	public BindingBuilder<T> toResolver(Class<Resolver<T>> type){
		Resolver<T> r = binder.getInstance(WiringSpecification.search(type));
		binding.setResolver(r);
		return this;
	}
	
	public BindingBuilder<T> annotatedWith(Class<? extends BindingSpecification> type){
		binder.removeBinding(binding);
		binding.addAnnotation(type);
		binder.addBinding(binding);
		return this;
	}
	

}
