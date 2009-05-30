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
}
