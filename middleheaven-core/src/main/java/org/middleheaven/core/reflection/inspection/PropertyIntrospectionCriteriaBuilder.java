package org.middleheaven.core.reflection.inspection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;

import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.util.collections.Enumerable;

public class PropertyIntrospectionCriteriaBuilder<T> extends MemberIntrospectionCriteriaBuilder<T,PropertyAccessor>{

	public PropertyIntrospectionCriteriaBuilder(IntrospectionCriteriaBuilder<T> builder) {
		super(builder);
	}

	public PropertyIntrospectionCriteriaBuilder<T> named(String propertyName) {
		super.addNameFilter(propertyName);
		return this;
	}


	@Override
	protected Enumerable<PropertyAccessor> getAllMembersInType(Class<T> type) {
		return Reflector.getReflector().getPropertyAccessors(type);
	}

	@Override
	protected int getModifiers(PropertyAccessor obj) {
		return Modifier.PUBLIC;
	}

	@Override
	protected String getName(PropertyAccessor obj) {
		return obj.getName();
	}

	@Override
	protected boolean isAnnotationPresent(PropertyAccessor obj, Class<? extends Annotation> annotationClass) {
		return obj.isAnnotadedWith(annotationClass);
	}
}
