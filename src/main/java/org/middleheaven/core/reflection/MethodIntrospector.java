package org.middleheaven.core.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class MethodIntrospector extends Introspector{

	private Method method;

	public MethodIntrospector(Method method) {
		this.method = method;
	}

	public String getName(){
		return method.getName();
	}
	
	public ClassIntrospector<?> getDeclaringClass(){
		return Introspector.of(method.getDeclaringClass());
	}

	@Override
	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		return method.getAnnotation(annotationClass);
	}

	@Override
	public <A extends Annotation> boolean isAnnotadedWith(
			Class<A> annotationClass) {
		return method.isAnnotationPresent(annotationClass);
	}
	
	public boolean isBridge(){
		return method.isBridge();
	}
	

} 
