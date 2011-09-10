package org.middleheaven.core.reflection.inspection;

import java.lang.annotation.Annotation;
import java.util.Comparator;

import org.middleheaven.core.reflection.MemberAccess;
import org.middleheaven.util.classification.BooleanClassifier;
import org.middleheaven.util.classification.LogicComposedClassifier;
import org.middleheaven.util.classification.LogicOperator;
import org.middleheaven.util.collections.EnhancedCollection;
import org.middleheaven.util.collections.Walker;

public abstract class MemberIntrospectionCriteriaBuilder<T, M> {

	protected Comparator<M> comparator = null;
	private IntrospectionCriteriaBuilder<T> builder;
	protected final LogicComposedClassifier<M> logicClassifier =
		new LogicComposedClassifier<M>(LogicOperator.and());
	
	protected MemberIntrospectionCriteriaBuilder(IntrospectionCriteriaBuilder<T> builder) {
		this.builder = builder;
	}
	
	protected void add(BooleanClassifier<M> filter) {
		logicClassifier.add(filter);
	}
	
	public M retrive(){
		return retriveAll().isEmpty() ? null : retriveAll().iterator().next();
	}
	
	public void each(Walker<M> walker){
		retriveAll().each(walker);
	}
	
	protected void addNameFilter(final String name){
		logicClassifier.add(new BooleanClassifier<M>(){

			@Override
			public Boolean classify(M obj) {
				return getName(obj).equalsIgnoreCase(name);
			}
			
		});
	}

	protected abstract String getName(M obj);
	
	protected void addWithAccessFilter(final MemberAccess ... acesses){
		logicClassifier.add(new BooleanClassifier<M>(){

			@Override
			public Boolean classify(M obj) {
				boolean result = false;
				for (MemberAccess a : acesses){
					result = result | a.is(getModifiers(obj));
				}
				return result;
			}
			
		});
	}
	
	protected abstract int getModifiers(M obj);
	protected abstract boolean isAnnotationPresent(M obj , Class<? extends Annotation> annotationType);
	
	protected void addAnnotatedWithFilter(final Class<? extends Annotation> ... annotations){
		logicClassifier.add(new BooleanClassifier<M>(){
			
			@Override
			public Boolean classify(M c) {
				for (Class<? extends Annotation> a : annotations){
					if (isAnnotationPresent(c,a)){
						return true;
					}
				}
				return false;
			}
		
		});
	}
	
	public EnhancedCollection<M> retriveAll(){
		EnhancedCollection<M> result = getAllMembersInType(builder.type);
		
		if(logicClassifier!=null){
			result = result.findAll(logicClassifier);
		} 
		
		if (comparator!=null){
			result = result.sort(comparator);
		}

		return result;
	}

	protected abstract EnhancedCollection<M> getAllMembersInType(Class<T> type);
}
