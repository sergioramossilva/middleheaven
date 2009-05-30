package org.middleheaven.core.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * High level abstraction for handling types metadata.
 */
public abstract class Introspector {

	public static <T> ObjectInstrospector<T> of(T object){
		return new ObjectInstrospector<T>(object);
	}
	

	public static <T> ClassIntrospector<T> of(Class<T> type){
		return new ClassIntrospector<T>(type);
	}

	public static PackageIntrospector of(Package typePackage) {
		return new PackageIntrospector(typePackage);
	}
	
	public static MethodIntrospector of(Method method) {
		return new MethodIntrospector(method);
	}
	
	public static FieldIntrospector of(Field field) {
		return new FieldIntrospector(field);
	}
	
	// package
	Introspector(){}
	
	public abstract <A extends Annotation> boolean isAnnotadedWith(Class<A> annotationClass);

	public abstract <A extends Annotation> A getAnnotation(Class<A> annotationClass);
}
