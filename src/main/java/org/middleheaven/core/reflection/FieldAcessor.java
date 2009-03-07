package org.middleheaven.core.reflection;

import java.lang.annotation.Annotation;

public interface FieldAcessor {

	public Object getValue(Object target) throws ReflectionException;

	public void setValue(Object target, Object value) throws ReflectionException;

	public String getName();

	public Class<?> getParentClass();

	public Class<?> getValueType();

	public <A extends Annotation> boolean isAnnotatedWith(Class<A> annotationClass);

	public <A extends Annotation> A getAnnotation(Class<A> annotationClass);

}
