package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

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
		this.params = params;
		this.annotations = annotations;
	}

	public Class<T> getContract() {
		return contract;
	}

	public Set<Annotation> getSpecifications() {
		return annotations;
	}
}
