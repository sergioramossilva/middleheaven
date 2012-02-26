package org.middleheaven.core.wiring;
import java.lang.annotation.Annotation;
import java.util.Map;

import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.wiring.annotations.Default;
import org.middleheaven.core.wiring.annotations.ScopeSpecification;
import org.middleheaven.core.wiring.annotations.Shared;

public class BindingBuilder<T> {

	protected Binding binding;
	protected EditableBinder binder;
	
	BindingBuilder (EditableBinder binder , Class<T> type){
		this.binder = binder;
		this.binding = new Binding();

		binding.setAbstractType(type);
	}

	public BindingBuilder<T> to(Class<? extends T> type){
		

		BeanModel model = binder.getBeanModel(type);
		
		binding.addParams(model.getParams());
		
		binding.setResolver(FactoryResolver.instanceFor(type,binder));
		binder.addBinding(binding);
		return this;
	}
	

	public BindingBuilder<T> named(String name){
		binding.addParam("name", name);
		return this;
	}
	
	public BindingBuilder<T> lazy(){
		binding.setLazy(true);
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
		
		if (this.binding.getScope() == null || this.binding.getScope().equals(Default.class)){
			this.in(Shared.class);
		}
		
		binder.addBinding(binding);
		
		return this;

	} 
	
	public BindingBuilder<T> toResolver(Class<Resolver<T>> type){
		Resolver<T> r = binder.getInstance(WiringSpecification.search(type));
		binding.setResolver(r);
		
		binder.addBinding(binding);
		return this;
	}

	/**
	 * @param params
	 */
	public BindingBuilder<T> withParams(Map<String, Object> params) {
		binding.addParams(params);
		return this;
	}
	
	/**
	 * @param params
	 */
	public BindingBuilder<T> withParam(String key, Object value) {
		binding.addParam(key,value);
		return this;
	}
	

	

}
