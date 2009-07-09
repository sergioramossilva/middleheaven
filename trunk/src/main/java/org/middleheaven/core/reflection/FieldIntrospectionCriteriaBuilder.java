package org.middleheaven.core.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.EnhancedCollection;

public class FieldIntrospectionCriteriaBuilder<T> extends MemberIntrospectionCriteriaBuilder<T,Field>{


	FieldIntrospectionCriteriaBuilder(IntrospectionCriteriaBuilder<T> builder) {
		super(builder);
	}
	
	public FieldIntrospectionCriteriaBuilder<T> named(final String name) {
		super.addNameFilter(name);
		return this;
	}

	/**
	 * annotated 
	 * @param annotations
	 * @return
	 */
	public FieldIntrospectionCriteriaBuilder<T> annotatedWith(final Class<? extends Annotation> ... annotations) {
		addAnnotatedWithFilter(annotations);
		return this;
	}
	
	public FieldIntrospectionCriteriaBuilder<T> withAccess(final MemberAccess ... acesses){
		addWithAccessFilter(acesses);
		return this;

	}
	
	@Override
	protected int getModifiers(Field obj) {
		return obj.getModifiers();
	}

	@Override
	protected EnhancedCollection<Field> getAllMembersInType(Class<T> type) {
		return CollectionUtils.enhance(type.getDeclaredFields());
	}

	@Override
	protected boolean isAnnotationPresent(Field obj,
			Class<? extends Annotation> annotationType) {
		return obj.isAnnotationPresent(annotationType);
	}

	@Override
	protected String getName(Field obj) {
		return obj.getName();
	}


}
