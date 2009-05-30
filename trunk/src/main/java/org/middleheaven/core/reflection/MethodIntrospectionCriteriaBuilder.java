package org.middleheaven.core.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Comparator;

import org.middleheaven.util.classification.BooleanClassifier;
import org.middleheaven.util.classification.LogicComposedClassifier;
import org.middleheaven.util.classification.LogicOperator;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.EnhancedCollection;

public class MethodIntrospectionCriteriaBuilder<T>{

	private IntrospectionCriteriaBuilder<T> builder;
	private Comparator<Method> comparator;
	private final LogicComposedClassifier<Method> logicClassifier =
		new LogicComposedClassifier<Method>(LogicOperator.and());
	
	MethodIntrospectionCriteriaBuilder(IntrospectionCriteriaBuilder<T> builder){
		this.builder = builder;
	}
	
	public MethodIntrospectionCriteriaBuilder<T> annotathedWith(final Class<? extends Annotation> ... annotations) {

		logicClassifier.add(new BooleanClassifier<Method>(){
			
			@Override
			public Boolean classify(Method c) {
				for (Class<? extends Annotation> a : annotations){
					if (c.isAnnotationPresent(a)){
						return true;
					}
				}
				return false;
			}
		
		});
		return this;
	}
	
	public MethodIntrospectionCriteriaBuilder<T> sortedByQuantityOfParameters(){
		this.comparator = new Comparator<Method>(){

			@Override
			public int compare(Method a, Method b) {
				return a.getParameterTypes().length - b.getParameterTypes().length;
			}

		};
		return this;
	}

	public MethodIntrospectionCriteriaBuilder<T> withAccess(final MemberAccess ... acesses){
		
		logicClassifier.add(new BooleanClassifier<Method>(){

			@Override
			public Boolean classify(Method obj) {
				boolean result = false;
				for (MemberAccess a : acesses){
					result = result | a.is(obj.getModifiers());
				}
				return result;
			}
			
		});
		return this;
	}
	
	public MethodIntrospectionCriteriaBuilder<T> notInheritFromObject() {
		logicClassifier.add(new BooleanClassifier<Method>(){

			@Override
			public Boolean classify(Method obj) {
				return !obj.getDeclaringClass().equals(Object.class);
			}
			
		});
		return this;
	}
	
	
	public EnhancedCollection<Method> retrive() {
		

		EnhancedCollection<Method> result = CollectionUtils.enhance(ReflectionUtils.getMethods(builder.type));
		
		if(logicClassifier!=null){
			result = result.findAll(logicClassifier);
		} 
		
		if (comparator!=null){
			result = result.sort(comparator);
		}
		
		return result;
		
	}





}
