package org.middleheaven.core.reflection;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.middleheaven.util.classification.BooleanClassifier;


public class IntrospectionCriteriaBuilder<T> {

	Class<T> type;

	IntrospectionCriteriaBuilder(Class<T> type){
		this.type = type;
	}
	
	public ConstructorIntrospectionCriteriaBuilder<T> constructors(){
		return new ConstructorIntrospectionCriteriaBuilder<T>(this);
	}
	
	public MethodIntrospectionCriteriaBuilder<T> methods(){
		return new MethodIntrospectionCriteriaBuilder<T>(this);
	}
	

	public MethodIntrospectionCriteriaBuilder<T> staticMethods() {
		return new MethodIntrospectionCriteriaBuilder<T>(this)
		.match(new BooleanClassifier<Method>(){
			public Boolean classify (Method m){
				return Boolean.valueOf(Modifier.isStatic( m.getModifiers()));
			}
		});
	}

	public AnnotationIntrospectionCriteriaBuilder<T> annotations() {
		return new AnnotationIntrospectionCriteriaBuilder<T>(this);
	}

	public FieldIntrospectionCriteriaBuilder<T> fields() {
		return new FieldIntrospectionCriteriaBuilder<T>(this);
	}

	public PropertyIntrospectionCriteriaBuilder<T> properties() {
		return new PropertyIntrospectionCriteriaBuilder<T>(this);
	}

}
