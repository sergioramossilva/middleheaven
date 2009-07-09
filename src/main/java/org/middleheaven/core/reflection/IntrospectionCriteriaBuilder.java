package org.middleheaven.core.reflection;


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
