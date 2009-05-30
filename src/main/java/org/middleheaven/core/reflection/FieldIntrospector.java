package org.middleheaven.core.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class FieldIntrospector extends Introspector implements FieldAcessor {

	private Field field;

	public FieldIntrospector(Field field) {
		this.field = field;
	}

	public Object getValue(Object target) throws ReflectionException{
		try {
			field.setAccessible(true);
			return field.get(target);
		} catch (IllegalArgumentException e) {
			throw new ReflectionException(e);
		} catch (IllegalAccessException e) {
			throw new ReflectionException(e);
		}
	}

	public void setValue(Object target, Object value )  throws ReflectionException{
		try {
			field.set(target,value);
		} catch (IllegalArgumentException e) {
			throw new ReflectionException(e);
		} catch (IllegalAccessException e) {
			throw new ReflectionException(e);
		}
	}

	public String toString(){
		return this.getName();
	}
	
	public String getName() {
		return field.getName();
	}

	public Class<?> getDeclaringClass(){
		return field.getDeclaringClass();
	}

	public Class<?> getValueType(){
		return field.getType();
	}

	public <A extends Annotation> boolean isAnnotadedWith(Class<A> annotationClass) {
		return field.isAnnotationPresent(annotationClass);
	}

	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		return field.getAnnotation(annotationClass);
	}

}
