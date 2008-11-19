package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.util.Set;

public class InterceptionContext <T>{

	private T object;
	private WiringSpecification<T> specification;

	public InterceptionContext(WiringSpecification<T> query) {
		this.specification = query;
	}

	protected T getObject() {
		return object;
	}

	protected void setObject(T object) {
		this.object = object;
	}

	protected WiringSpecification<T> getWiringSpecification(){
		return specification;
	}
	
	protected Class<?> getTarget() {
		return specification.getContract();
	}

	protected Set<Annotation> getAnnotations() {
		return specification.getSpecifications();
	}
}
