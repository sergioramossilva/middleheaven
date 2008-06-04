package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.util.Set;

public class InterceptionContext <T>{

	private T object;
	private Class<?> target;
	private Set<Annotation> annotations;
	
	public InterceptionContext(Class<?> target, Set<Annotation> annotations) {
		this.target = target;
		this.annotations = annotations;
	}

	protected T getObject() {
		return object;
	}

	protected void setObject(T object) {
		this.object = object;
	}

	protected Class<?> getTarget() {
		return target;
	}

	protected Set<Annotation> getAnnotations() {
		return annotations;
	}
}
