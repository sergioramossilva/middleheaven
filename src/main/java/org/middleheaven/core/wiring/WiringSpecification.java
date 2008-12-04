package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class WiringSpecification<T> {

	private Class<T> contract;
	private Set<Annotation> annotations;
	private Map<String, String> params;

	public static <C> WiringSpecification<C> search(Class<C> contract) {
		Map<String,String> params = Collections.emptyMap();
		Set<Annotation> empty = Collections.emptySet();
		return search(contract, params, empty);
	}

	public static <C> WiringSpecification<C> search(Class<C> contract, Map<String,String> params) {
		Set<Annotation> empty = Collections.emptySet();
		return search(contract, params, empty);
	}

	public static <C> WiringSpecification<C> search(Class<C> contract, Map<String,String> params , Set<Annotation> annotations) {
		return new WiringSpecification<C>(contract, params,annotations);
	}	

	public static <C> WiringSpecification<C> search(Class<C> contract, Set<Annotation> annotations) {
		Map<String,String> params = Collections.emptyMap();
		return search(contract, params, annotations);
	}

	private WiringSpecification(Class<T> contract, Map<String,String> params , Set<Annotation> annotations) {
		this.contract = contract;
		this.annotations = new HashSet<Annotation> (annotations);
		this.params = params;

		for (Annotation a : this.annotations){
			if (Name.class.isAssignableFrom(a.annotationType())){
				try{
					this.params.put("name", ((Name)a).value());
				} catch (UnsupportedOperationException e){
					// the map is not editable
					this.params = new TreeMap<String,String>(params);
					// try again
					this.params.put("name", ((Name)a).value());
				}
			}
		}
	}

	public Map<String, String > getParams(){
		return Collections.unmodifiableMap(this.params);
	}

	public String getParam(String key){
		return params.get(key);
	}

	public Class<T> getContract() {
		return contract;
	}

	public Set<Annotation> getSpecifications() {
		return Collections.unmodifiableSet(annotations);
	}
}
