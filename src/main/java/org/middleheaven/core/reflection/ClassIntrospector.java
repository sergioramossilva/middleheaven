package org.middleheaven.core.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.middleheaven.util.classification.Classifier;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.EnhancedArrayList;
import org.middleheaven.util.collections.EnhancedCollection;
import org.middleheaven.util.collections.TransformCollection;

public class ClassIntrospector<T> extends Introspector{

	private Class<T> type;

	public ClassIntrospector(Class<T> type) {
		this.type = type;
	}

	public T cast (Object object){
		return type.cast(object);
	}

	public String getCanonicalName (){
		return type.getCanonicalName();
	}

	public String getName (){
		return type.getName();
	}

	public String getSimpleName (){
		return type.getSimpleName();
	}

	public IntrospectionCriteriaBuilder<T> inspect(){
		return new IntrospectionCriteriaBuilder<T>(this.type);
	}
	
	public PackageIntrospector getPackage (){
		return Introspector.of(type.getPackage());
	}

	public T newInstance (Object ... args){
		return ReflectionUtils.newInstance(this.type, args);
	}

	public T newInstance (ClassLoader otherClassLoader){
		return ReflectionUtils.newInstance(this.type, otherClassLoader);
	}
	
	public T newProxyInstance(ProxyHandler handler){
		if(!type.isInterface()){
			throw new UnsupportedOperationException("Cannot proxy " + type.getName() + " as interface");
		}
		return ReflectionUtils.proxy(type, handler);
	} 
	
	
	public EnhancedCollection<PropertyAccessor> properties(){
		return new EnhancedArrayList<PropertyAccessor>(ReflectionUtils.getPropertyAccessors(type));
	}
	
	public boolean isInstance (Object obj){
		return type.isInstance(obj);
	}

	public EnhancedCollection<MethodIntrospector> methods(){
		return CollectionUtils.enhance(new TransformCollection<Method, MethodIntrospector>(
				ReflectionUtils.getMethods(type) , 
				new Classifier< MethodIntrospector,Method>(){

					@Override
					public MethodIntrospector classify(Method obj) {
						return Introspector.of(obj);
					}
				}
		));
	}

	@Override
	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		return type.getAnnotation(annotationClass);
	}

	@Override
	public <A extends Annotation> boolean isAnnotadedWith(Class<A> annotationClass) {
		return type.isAnnotationPresent(annotationClass);
	}



}
