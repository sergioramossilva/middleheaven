package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.util.Set;

public final class InterceptionContext <T>{

	private T object;
	private WiringSpecification<T> specification;

	public InterceptionContext(WiringSpecification<T> query) {
		this.specification = query;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public WiringSpecification<T> getWiringSpecification(){
		return specification;
	}
	
	public Class<?> getTarget() {
		return specification.getContract();
	}

}
