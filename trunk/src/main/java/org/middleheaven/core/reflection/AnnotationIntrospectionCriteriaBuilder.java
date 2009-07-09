package org.middleheaven.core.reflection;

import java.lang.annotation.Annotation;

import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.EnhancedSet;

public class AnnotationIntrospectionCriteriaBuilder<T> {

	private IntrospectionCriteriaBuilder<T> introspectionCriteriaBuilder;

	public AnnotationIntrospectionCriteriaBuilder(IntrospectionCriteriaBuilder<T> introspectionCriteriaBuilder) {
		this.introspectionCriteriaBuilder = introspectionCriteriaBuilder;
	}
	
	public EnhancedSet<Annotation> retrive(){
		return CollectionUtils.enhance(ReflectionUtils.getAnnotations(introspectionCriteriaBuilder.type));
	}

}
