package org.middleheaven.core.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.EnhancedCollection;

public class MethodIntrospector extends Introspector{

	private Method method;

	MethodIntrospector(Method method) {
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
	
	public EnhancedCollection<Annotation> getAnnotatedAnnotations(Class<? extends Annotation>  metaAnnotation) {
		Set<Annotation> result = new HashSet<Annotation>(); 

		Annotation[] all = method.getDeclaredAnnotations();
		for (Annotation a : all){
			if (a.annotationType().isAnnotationPresent(metaAnnotation)){
				result.add(a);
			}
		}
		
		return CollectionUtils.enhance(result);
	}
	
	public <R> R invoke(Class<R> returnType,Object target, Object ... params) {
		try {
			this.method.setAccessible(true);
			Object obj = this.method.invoke(target, params);
			if (obj != null && !Void.class.isInstance(returnType)){
				return returnType.cast(obj);
			} else {
				return null;
			}
		} catch (SecurityException e) {
			throw new IllegalAccessReflectionException(e);
		} catch (IllegalArgumentException e) {
			throw new IllegalAccessReflectionException(e);
		} catch (InvocationTargetException e) {
			throw new InvocationTargetReflectionException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalAccessReflectionException(e);
		}
	}

} 
