package org.middleheaven.core.wiring;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.wiring.annotations.Default;
import org.middleheaven.util.collections.ParamsMap;


public class ScoopingModel {

	private List<Class<?>> scopes = new LinkedList<Class<?>>();
	Map<String,String> params = new ParamsMap();
	
	public ScoopingModel(){

	}

	public void addScope(Class<?> scope) {
		this.scopes.add(scope);
	}
	
	public void addParam(String name, String value) {
		this.params.put(name,value);
	}
	
	public void addParams(Map<String,String> other) {
		this.params.putAll(other);
	}
	
	public <T> void addToScope(EditableBinder binder, Object object) {
		@SuppressWarnings("unchecked") T instance = (T) object;
		@SuppressWarnings("unchecked") Class<T> type = (Class<T>) object.getClass();

		if (scopes.isEmpty()){
			// add to default
			scopes.add(Default.class);
		} 
		
		for (Class<?> scope : scopes){
			Binding binding = new Binding();
			binding.setStartType(type);
			binding.setTargetScope(scope);
			binding.setResolver(new InstanceResolver<T>(instance));
		
			// determine the interface beeing implemented
			final Class<?>[] interfaces = Introspector.of(type).getDeclaredInterfaces();
			
			if (interfaces.length > 0){
				 
				Class<?> i = interfaces[0];
				 
				WiringSpecification spec = WiringSpecification.search(i,params);
				
				binder.getScopePool(binding).add(spec, instance);
			}

			
			
		}



	}

	



}
