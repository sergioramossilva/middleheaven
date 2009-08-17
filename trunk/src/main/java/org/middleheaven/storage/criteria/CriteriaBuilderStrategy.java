package org.middleheaven.storage.criteria;

import org.middleheaven.storage.QualifiedName;

public abstract class CriteriaBuilderStrategy<T, B extends CriteriaBuilderStrategy<T,B>> {

	public abstract CriteriaBuilderStrategy<T,B> isEqual(Object instance);

	public abstract CriteriaBuilderStrategy<T,B> isSame(Object instance);

	public abstract Constraint<T,B> and(String name);

	public abstract Constraint<T,B> or(String name);

	public abstract OrderingConstrain<T,B> orderBy(String name);

	protected abstract Class<?> getCurrentType();
	

	 abstract void addCriterion(Criterion criterion);

	 abstract void addOrderingCriterion(OrderingCriterion criterion);
}