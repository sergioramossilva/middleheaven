package org.middleheaven.core.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Comparator;

import org.middleheaven.util.classification.BooleanClassifier;
import org.middleheaven.util.classification.LogicComposedClassifier;
import org.middleheaven.util.classification.LogicOperator;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.EnhancedCollection;

public class ConstructorIntrospectionCriteriaBuilder<T>{

	private IntrospectionCriteriaBuilder<T> builder;
	private Comparator<Constructor<T>> comparator;
	private BooleanClassifier<Constructor<T>> annotationPresent;
	private final LogicComposedClassifier<Constructor<T>> logicClassifier =
		new LogicComposedClassifier<Constructor<T>>(LogicOperator.and());
	
	ConstructorIntrospectionCriteriaBuilder(IntrospectionCriteriaBuilder<T> builder){
		this.builder = builder;
	}

	public ConstructorIntrospectionCriteriaBuilder<T> annotathedWith(final Class<? extends Annotation> ... annotations) {

		annotationPresent = new BooleanClassifier<Constructor<T>>(){

			@Override
			public Boolean classify(Constructor<T> c) {
				for (Class<? extends Annotation> a : annotations){
					if (c.isAnnotationPresent(a)){
						return true;
					}
				}
				return false;
			}

		};
		return this;
	}

	public ConstructorIntrospectionCriteriaBuilder<T> withAccess(final MemberAccess ... acesses){

		logicClassifier.add(new BooleanClassifier<Constructor<T>>(){

			@Override
			public Boolean classify(Constructor<T> obj) {
				boolean result = false;
				for (MemberAccess a : acesses){
					result = result | a.is(obj.getModifiers());
				}
				return result;
			}

		});
		return this;
	}

	public ConstructorIntrospectionCriteriaBuilder<T> sortedByQuantityOfParameters(){
		this.comparator = new Comparator<Constructor<T>>(){

			@Override
			public int compare(Constructor<T> a, Constructor<T> b) {
				return a.getParameterTypes().length - b.getParameterTypes().length;
			}

		};
		return this;
	}


	public EnhancedCollection<Constructor<T>> retrive() {
		@SuppressWarnings("unchecked") Constructor<T>[] constructors = (Constructor<T>[])builder.type.getConstructors();

		EnhancedCollection<Constructor<T>> result = CollectionUtils.enhance(constructors);

		if(annotationPresent!=null){
			result = result.findAll(annotationPresent);
		} 

		if (comparator!=null){
			result = result.sort(comparator);
		}

		return result;

	}

}
